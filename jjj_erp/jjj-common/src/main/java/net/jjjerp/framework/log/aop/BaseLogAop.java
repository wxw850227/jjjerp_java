

package net.jjjerp.framework.log.aop;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminLoginLog;
import net.jjjerp.common.entity.shop.AdminOptLog;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.service.shop.ShopLoginLogService;
import net.jjjerp.common.service.shop.ShopOptLogService;
import net.jjjerp.common.settings.vo.StoreVo;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.config.properties.SpringBootJjjAopProperties;
import net.jjjerp.framework.common.api.ApiCode;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.common.bean.ClientInfo;
import net.jjjerp.framework.ip.service.IpAddressService;
import net.jjjerp.framework.log.annotation.Module;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.framework.log.annotation.OperationLogIgnore;
import net.jjjerp.framework.log.bean.OperationLogInfo;
import net.jjjerp.framework.log.bean.RequestInfo;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.framework.shiro.util.JwtUtil;
import net.jjjerp.framework.shiro.vo.LoginShopUserTokenVo;
import net.jjjerp.framework.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.fusesource.jansi.Ansi;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * Controller Aop 抽象类
 * 获取响应结果信息
 * <p>
 * 日志输出类型：print-type
 * 1. 请求和响应分开，按照执行顺序打印
 * 2. ThreadLocal线程绑定，方法执行结束时，连续打印请求和响应日志
 * 3. ThreadLocal线程绑定，方法执行结束时，同时打印请求和响应日志
 * </p>
 */
@Slf4j
public abstract class BaseLogAop {

    @Autowired
    protected ShopOptLogService shopOptLogService;
    @Autowired
    private IpAddressService ipAddressService;
    @Autowired
    private ShopLoginLogService shopLoginLogService;
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 本地线程变量，保存请求参数信息到当前线程中
     */
    protected static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RequestInfo> requestInfoThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<OperationLogInfo> operationLogThreadLocal = new ThreadLocal<>();

    /**
     * 请求ID
     */
    private static final String REQUEST_ID = "requestId";
    /**
     * 零
     */
    private static final int ZERO = 0;
    /**
     * 截取字符串的最多长度
     */
    private static final int MAX_LENGTH = 300;
    /**
     * 登录日志：登录类型
     */
    private static final int LOGIN_TYPE = 1;
    /**
     * 登录日志：登出类型
     */
    private static final int LOGOUT_TYPE = 2;

    /**
     * 项目上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Aop日志配置
     */
    protected SpringBootJjjAopProperties.LogAopConfig logAopConfig;

    /**
     * 日志打印类型
     */
    protected SpringBootJjjAopProperties.LogPrintType logPrintType;

    /**
     * 是否启用请求ID
     */
    protected boolean enableRequestId;

    /**
     * requestId生成类型
     */
    protected SpringBootJjjAopProperties.RequestIdType requestIdType;

    /**
     * Aop操作日志配置
     */
    protected SpringBootJjjAopProperties.OperationLogConfig operationLogConfig;

    /**
     * Aop登录日志配置
     */
    protected SpringBootJjjAopProperties.LoginLogConfig loginLogConfig;

    @Autowired
    public void setSpringBootJjjAopProperties(SpringBootJjjAopProperties springBootJjjAopProperties) {
        logAopConfig = springBootJjjAopProperties.getLog();
        logPrintType = logAopConfig.getLogPrintType();
        enableRequestId = logAopConfig.isEnableRequestId();
        requestIdType = logAopConfig.getRequestIdType();
        operationLogConfig = springBootJjjAopProperties.getOperationLog();
        loginLogConfig = springBootJjjAopProperties.getLoginLog();
        log.debug("logAopConfig = " + logAopConfig);
        log.debug("logPrintType = " + logPrintType);
        log.debug("enableRequestId = " + enableRequestId);
        log.debug("requestIdType = " + requestIdType);
        log.debug("operationLogConfig = " + operationLogConfig);
        log.debug("loginLogConfig = " + loginLogConfig);
        log.debug("contextPath = " + contextPath);
    }

    /**
     * 环绕通知
     * 方法执行前打印请求参数信息
     * 方法执行后打印响应结果信息
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     * 异常通知方法
     *
     * @param joinPoint
     * @param exception
     */
    public abstract void afterThrowing(JoinPoint joinPoint, Exception exception);

    /**
     * 设置请求ID
     *
     * @param requestInfo
     */
    protected abstract void setRequestId(RequestInfo requestInfo);

    /**
     * 获取请求信息对象
     *
     * @param requestInfo
     */
    protected abstract void getRequestInfo(RequestInfo requestInfo);

    /**
     * 获取响应结果对象
     *
     * @param result
     */
    protected abstract void getResponseResult(Object result);

    /**
     * 请求响应处理完成之后的回调方法
     *
     * @param requestInfo
     * @param operationLogInfo
     * @param result
     * @param exception
     */
    protected abstract void finish(RequestInfo requestInfo, OperationLogInfo operationLogInfo, Object result, Exception exception);

    /**
     * 处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求相关信息
        try {
            // 获取当前的HttpServletRequest对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // HTTP请求信息对象
            RequestInfo requestInfo = new RequestInfo();

            // 请求路径 /api/foobar/add
            String path = request.getRequestURI();
            requestInfo.setPath(path);
            // 获取实际路径 /foobar/add
            String realPath = getRealPath(path);
            requestInfo.setRealPath(realPath);

            // 排除路径
            Set<String> excludePaths = logAopConfig.getExcludePaths();
            // 请求路径
            if (handleExcludePaths(excludePaths, realPath)) {
                return joinPoint.proceed();
            }

            // 获取请求类名和方法名称
            Signature signature = joinPoint.getSignature();

            // 获取真实的方法对象
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();

            // 处理操作日志信息
            handleOperationLogInfo(method);

            // IP地址
            String ip = IpUtil.getRequestIp();
            requestInfo.setIp(ip);

            // 获取请求方式
            String requestMethod = request.getMethod();
            requestInfo.setRequestMethod(requestMethod);

            // 获取请求内容类型
            String contentType = request.getContentType();
            requestInfo.setContentType(contentType);

            // 判断控制器方法参数中是否有RequestBody注解
            Annotation[][] annotations = method.getParameterAnnotations();
            boolean isRequestBody = isRequestBody(annotations);
            requestInfo.setRequestBody(isRequestBody);

            AnnotatedType[] annotatedTypes = method.getAnnotatedParameterTypes();

            // 获取Shiro注解值，并记录到map中
            handleShiroAnnotationValue(requestInfo, method);

            // 设置请求参数
            Object requestParamObject = getRequestParamObject(joinPoint, request, requestMethod, contentType, isRequestBody);
            requestInfo.setParam(requestParamObject);
            requestInfo.setTime(DateUtil.getDateTimeString(new Date()));

            String model = JwtTokenUtil.getModel(path);

            // 获取请求头token
            String token = request.getHeader(JwtTokenUtil.getTokenName(model));
            requestInfo.setToken(token);
            if (StringUtils.isNotBlank(token)) {
                requestInfo.setTokenMd5(DigestUtils.md5Hex(token));
            }

            // 用户浏览器代理字符串
            requestInfo.setUserAgent(request.getHeader(CommonConstant.USER_AGENT));

            // 记录请求ID
            setRequestId(requestInfo);

            // 调用子类重写方法，控制请求信息日志处理
            getRequestInfo(requestInfo);
        } catch (Exception e) {
            log.error("请求日志AOP处理异常", e);
        }

        // 执行目标方法,获得返回值
        // 方法异常时，会调用子类的@AfterThrowing注解的方法，不会调用下面的代码，异常单独处理
        Object result = joinPoint.proceed();
        try {
            // 调用子类重写方法，控制响应结果日志处理
            getResponseResult(result);
        } catch (Exception e) {
            log.error("处理响应结果异常", e);
        } finally {
            handleAfterReturn(result, null);
        }
        return result;
    }

    /**
     * 正常调用返回或者异常结束后调用此方法
     *
     * @param result
     * @param exception
     */
    protected void handleAfterReturn(Object result, Exception exception) {
        // 获取RequestInfo
        RequestInfo requestInfo = requestInfoThreadLocal.get();
        // 获取OperationLogInfo
        OperationLogInfo operationLogInfo = operationLogThreadLocal.get();
        // 调用抽象方法，是否保存日志操作，需要子类重写该方法，手动调用saveSysOperationLog
        finish(requestInfo, operationLogInfo, result, null);
        // 释放资源
        remove();
    }

    /**
     * 处理异常
     *
     * @param exception
     */
    public void handleAfterThrowing(Exception exception) {
        // 获取RequestInfo
        RequestInfo requestInfo = requestInfoThreadLocal.get();
        // 获取OperationLogInfo
        OperationLogInfo operationLogInfo = operationLogThreadLocal.get();
        // 调用抽象方法，是否保存日志操作，需要子类重写该方法，手动调用saveSysOperationLog
        finish(requestInfo, operationLogInfo, null, exception);
        // 释放资源
        remove();
    }


    private void handleOperationLogInfo(Method method) {
        // 设置控制器类名称和方法名称
        OperationLogInfo operationLogInfo = new OperationLogInfo()
                .setControllerClassName(method.getDeclaringClass().getName())
                .setControllerMethodName(method.getName());

        // 获取Module类注解
        Class<?> controllerClass = method.getDeclaringClass();
        Module module = controllerClass.getAnnotation(Module.class);
        if (module != null) {
            String moduleName = module.name();
            String moduleValue = module.value();
            if (StringUtils.isNotBlank(moduleValue)) {
                operationLogInfo.setModule(moduleValue);
            }
            if (StringUtils.isNotBlank(moduleName)) {
                operationLogInfo.setModule(moduleName);
            }
        }
        // 获取OperationLogIgnore注解
        OperationLogIgnore classOperationLogIgnore = controllerClass.getAnnotation(OperationLogIgnore.class);
        if (classOperationLogIgnore != null) {
            // 不记录日志
            operationLogInfo.setIgnore(true);
        }
        // 判断方法是否要过滤
        OperationLogIgnore operationLogIgnore = method.getAnnotation(OperationLogIgnore.class);
        if (operationLogIgnore != null) {
            operationLogInfo.setIgnore(true);
        }
        // 从方法上获取OperationLog注解
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        if (operationLog != null) {
            String operationLogName = operationLog.name();
            String operationLogValue = operationLog.value();
            if (StringUtils.isNotBlank(operationLogValue)) {
                operationLogInfo.setName(operationLogValue);
            }
            if (StringUtils.isNotBlank(operationLogName)) {
                operationLogInfo.setName(operationLogName);
            }
            operationLogInfo.setType(operationLog.type().getCode()).setRemark(operationLog.remark());
        }
        operationLogThreadLocal.set(operationLogInfo);
    }

    /**
     * 获取Shiro注解值，并记录到map中
     *
     * @param requestInfo
     * @param method
     */
    protected void handleShiroAnnotationValue(RequestInfo requestInfo, Method method) {
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null) {
            String[] requiresRolesValues = requiresRoles.value();
            if (ArrayUtils.isNotEmpty(requiresRolesValues)) {
                String requiresRolesString = Arrays.toString(requiresRolesValues);
                requestInfo.setRequiresRoles(requiresRolesString);
            }
        }

        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null) {
            String[] requiresPermissionsValues = requiresPermissions.value();
            if (ArrayUtils.isNotEmpty(requiresPermissionsValues)) {
                String requiresPermissionsString = Arrays.toString(requiresPermissionsValues);
                requestInfo.setRequiresPermissions(requiresPermissionsString);
            }
        }

        RequiresAuthentication requiresAuthentication = method.getAnnotation(RequiresAuthentication.class);
        if (requiresAuthentication != null) {
            requestInfo.setRequiresAuthentication(true);
        }

        RequiresUser requiresUser = method.getAnnotation(RequiresUser.class);
        if (requiresUser != null) {
            requestInfo.setRequiresUser(true);
        }

        RequiresGuest requiresGuest = method.getAnnotation(RequiresGuest.class);
        if (requiresGuest != null) {
            requestInfo.setRequiresGuest(true);
        }

    }

    /**
     * 处理请求ID
     *
     * @param requestInfo
     */
    protected void handleRequestId(RequestInfo requestInfo) {
        if (!enableRequestId) {
            return;
        }
        String requestId = null;
        if (SpringBootJjjAopProperties.RequestIdType.IDWORK == requestIdType) {
            requestId = IdWorker.getIdStr();
        } else if (SpringBootJjjAopProperties.RequestIdType.UUID == requestIdType) {
            requestId = UUIDUtil.getUuid();
        }
        // 设置请求ID
        MDC.put(REQUEST_ID, requestId);
        requestInfo.setRequestId(requestId);
    }

    /**
     * 处理请求参数
     *
     * @param requestInfo
     */
    protected void handleRequestInfo(RequestInfo requestInfo) {
        requestInfoThreadLocal.set(requestInfo);
        if (SpringBootJjjAopProperties.LogPrintType.NONE == logPrintType) {
            return;
        }
        // 获取请求信息
        String requestInfoString = formatRequestInfo(requestInfo);
        // 如果打印方式为顺序打印，则直接打印，否则，保存的threadLocal中
        if (SpringBootJjjAopProperties.LogPrintType.ORDER == logPrintType) {
            printRequestInfoString(requestInfoString);
        } else {
            threadLocal.set(requestInfoString);
        }
    }

    /**
     * 处理响应结果
     *
     * @param result
     */
    protected void handleResponseResult(Object result) {
        if (SpringBootJjjAopProperties.LogPrintType.NONE == logPrintType) {
            return;
        }
        if (result != null && result instanceof ApiResult) {
            ApiResult<?> apiResult = (ApiResult<?>) result;
            int code = apiResult.getCode();
            // 获取格式化后的响应结果
            String responseResultString = formatResponseResult(apiResult);
            if (SpringBootJjjAopProperties.LogPrintType.ORDER == logPrintType) {
                printResponseResult(code, responseResultString);
            } else {
                // 从threadLocal中获取线程请求信息
                String requestInfoString = threadLocal.get();
                // 如果是连续打印，则先打印请求参数，再打印响应结果
                if (SpringBootJjjAopProperties.LogPrintType.LINE == logPrintType) {
                    printRequestInfoString(requestInfoString);
                    printResponseResult(code, responseResultString);
                } else if (SpringBootJjjAopProperties.LogPrintType.MERGE == logPrintType) {
                    printRequestResponseString(code, requestInfoString, responseResultString);
                }
            }
        }
    }

    /**
     * 同时打印请求和响应信息
     *
     * @param code
     * @param requestInfoString
     * @param responseResultString
     */
    protected void printRequestResponseString(int code, String requestInfoString, String responseResultString) {
        if (code == ApiCode.SUCCESS.getCode()) {
            //log.info(requestInfoString + "\n" + responseResultString);
        } else {
            log.error(requestInfoString + "\n" + responseResultString);
        }
    }


    /**
     * 格式化请求信息
     *
     * @param requestInfo
     * @return
     */
    protected String formatRequestInfo(RequestInfo requestInfo) {
        String requestInfoString = null;
        try {
            if (logAopConfig.isRequestLogFormat()) {
                requestInfoString = "\n" + Jackson.toJsonStringNonNull(requestInfo, true);
            } else {
                requestInfoString = Jackson.toJsonStringNonNull(requestInfo);
            }
        } catch (Exception e) {
            log.error("格式化请求信息日志异常", e);
        }
        return AnsiUtil.getAnsi(Ansi.Color.GREEN, "requestInfo:" + requestInfoString);
    }

    /**
     * 打印请求信息
     *
     * @param requestInfoString
     */
    protected void printRequestInfoString(String requestInfoString) {
        log.info(requestInfoString);
    }

    /**
     * 格式化响应信息
     *
     * @param apiResult
     * @return
     */
    protected String formatResponseResult(ApiResult<?> apiResult) {
        String responseResultString = "responseResult:";
        try {
            if (logAopConfig.isResponseLogFormat()) {
                responseResultString += "\n" + Jackson.toJsonString(apiResult, true);
            } else {
                responseResultString += Jackson.toJsonString(apiResult);
            }
            int code = apiResult.getCode();
            if (code == ApiCode.SUCCESS.getCode()) {
                return AnsiUtil.getAnsi(Ansi.Color.BLUE, responseResultString);
            } else {
                return AnsiUtil.getAnsi(Ansi.Color.RED, responseResultString);
            }
        } catch (Exception e) {
            log.error("格式化响应日志异常", e);
        }
        return responseResultString;
    }

    /**
     * 打印响应信息
     *
     * @param code
     * @param responseResultString
     */
    protected void printResponseResult(int code, String responseResultString) {
        if (code == ApiCode.SUCCESS.getCode() || code == 1) {
            log.info(responseResultString);
        } else {
            log.error(responseResultString);
        }
    }

    /**
     * 获取请求参数JSON字符串
     *
     * @param joinPoint
     * @param request
     * @param requestMethod
     * @param contentType
     * @param isRequestBody
     */
    protected Object getRequestParamObject(ProceedingJoinPoint joinPoint, HttpServletRequest request, String requestMethod, String contentType, boolean isRequestBody) {
        Object paramObject = null;
        if (isRequestBody) {
            // POST,application/json,RequestBody的类型,简单判断,然后序列化成JSON字符串
            Object[] args = joinPoint.getArgs();
            paramObject = getArgsObject(args);
        } else {
            // 获取getParameterMap中所有的值,处理后序列化成JSON字符串
            Map<String, String[]> paramsMap = request.getParameterMap();
            paramObject = getParamJSONObject(paramsMap);
        }
        return paramObject;
    }

    /**
     * 判断控制器方法参数中是否有RequestBody注解
     *
     * @param annotations
     * @return
     */
    protected boolean isRequestBody(Annotation[][] annotations) {
        boolean isRequestBody = false;
        for (Annotation[] annotationArray : annotations) {
            for (Annotation annotation : annotationArray) {
                if (annotation instanceof RequestBody) {
                    isRequestBody = true;
                }
            }
        }
        return isRequestBody;
    }

    /**
     * 请求参数拼装
     *
     * @param args
     * @return
     */
    protected Object getArgsObject(Object[] args) {
        if (args == null) {
            return null;
        }
        // 去掉HttpServletRequest和HttpServletResponse
        List<Object> realArgs = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                continue;
            }
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            if (arg instanceof MultipartFile) {
                continue;
            }
            if (arg instanceof ModelAndView) {
                continue;
            }
            realArgs.add(arg);
        }
        if (realArgs.size() == 1) {
            return realArgs.get(0);
        } else {
            return realArgs;
        }
    }


    /**
     * 获取参数Map的JSON字符串
     *
     * @param paramsMap
     * @return
     */
    protected JSONObject getParamJSONObject(Map<String, String[]> paramsMap) {
        if (MapUtils.isEmpty(paramsMap)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String[]> kv : paramsMap.entrySet()) {
            String key = kv.getKey();
            String[] values = kv.getValue();
            // 没有值
            if (values == null) {
                jsonObject.put(key, null);
            } else if (values.length == 1) {
                // 一个值
                jsonObject.put(key, values[0]);
            } else {
                // 多个值
                jsonObject.put(key, values);
            }
        }
        return jsonObject;
    }

    /**
     * 处理排除路径，匹配返回true，否则返回false
     *
     * @param excludePaths 排除路径
     * @param realPath     请求实际路径
     * @return
     */
    protected boolean handleExcludePaths(Set<String> excludePaths, String realPath) {
        if (CollectionUtils.isEmpty(excludePaths) || StringUtils.isBlank(realPath)) {
            return false;
        }
        // 如果是排除路径，则跳过
        if (excludePaths.contains(realPath)) {
            return true;
        }
        return false;
    }

    /**
     * 获取实际路径
     *
     * @param requestPath
     * @return
     */
    private String getRealPath(String requestPath) {
        // 如果项目路径不为空，则去掉项目路径，获取实际访问路径
        if (StringUtils.isNotBlank(contextPath)) {
            return requestPath.substring(contextPath.length());
        }
        return requestPath;
    }

    /**
     * 异步保存系统操作日志
     *
     * @param requestInfo
     * @param operationLogInfo
     * @param result
     * @param exception
     */
    @Async
    protected void saveShopOperationLog(RequestInfo requestInfo, OperationLogInfo operationLogInfo, Object result, Exception exception) {
        try {
            Integer shopUserId = ShopLoginUtil.getUserId();
            if(shopUserId == null){
                return;
            }
            JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
            StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
            // 如果不记录操作日志，则跳过
            if (!storeVo.getIsGetLog()) {
                return;
            }
            // 排除路径
            Set<String> excludePaths = operationLogConfig.getExcludePaths();
            // 请求路径
            if (handleExcludePaths(excludePaths, requestInfo.getRealPath())) {
                return;
            }

            // 操作日志
            AdminOptLog adminOptLog = new AdminOptLog();
            // 设置操作日志信息
            if (operationLogInfo != null) {
                // 如果类或方法上标注有OperationLogIgnore，则跳过
                if (operationLogInfo.isIgnore()) {
                    return;
                }
            }
            // 设置请求参数信息
            if (requestInfo != null) {
                adminOptLog.setIp(requestInfo.getIp())
                        .setUrl(requestInfo.getPath())
                        .setRequestType(requestInfo.getRequestMethod());

                // 设置参数字符串
                adminOptLog.setContent(Jackson.toJsonStringNonNull(requestInfo.getParam()));
                // Customer-Agent
                String userAgent = requestInfo.getUserAgent();
                if (StringUtils.isNotBlank(userAgent)) {
                    adminOptLog.setAgent(StringUtils.substring(userAgent, ZERO, MAX_LENGTH));
                }
                ClientInfo clientInfo = ClientInfoUtil.get(requestInfo.getUserAgent());
                if (clientInfo != null) {
                    adminOptLog.setBrowser(clientInfo.getBrowserName());
                }
            }

            // 设置当前登录信息
            adminOptLog.setShopUserId(shopUserId.intValue());
            // 保存日志到数据库
            shopOptLogService.save(adminOptLog);
        } catch (Exception e) {
            if (e instanceof JWTDecodeException) {
                JWTDecodeException jwtDecodeException = (JWTDecodeException) e;
                throw jwtDecodeException;
            }
            log.error("保存系统操作日志失败", e);
        }
    }

    /**
     * 异步保存系统登录日志
     *
     * @param requestInfo
     * @param operationLogInfo
     * @param result
     * @param exception
     */
    @Async
    protected void saveShopLoginLog(RequestInfo requestInfo, OperationLogInfo operationLogInfo, Object result, Exception exception) {
        try {
            String realPath = requestInfo.getRealPath();
            if (StringUtils.isBlank(realPath)) {
                return;
            }

            boolean flag = false;
            String optName = "";
            // 判断是否是登录路径
            if (realPath.equals(loginLogConfig.getLoginPath())) {
                flag = true;
                optName = "登录成功";
            } else if (realPath.equals(loginLogConfig.getLogoutPath())) {
                flag = true;
                optName = "退出成功";
            }

            // 判断登录登出结果
            if (flag && result != null && result instanceof ApiResult) {
                ApiResult<?> apiResult = (ApiResult<?>) result;
                if (apiResult.getCode() == 1) {
                    AdminLoginLog adminLoginLog = new AdminLoginLog();
                    adminLoginLog.setResult(optName);
                    adminLoginLog.setIp(requestInfo.getIp());
                    if ("登录成功".equals(optName)) {
                        LoginShopUserTokenVo loginShopUserVo = (LoginShopUserTokenVo)apiResult.getData();
                        JSONObject paramObject = (JSONObject)requestInfo.getParam();
                        adminLoginLog.setUsername(paramObject.getString("username"));
                        adminLoginLog.setAppId(loginShopUserVo.getLoginShopUserVo().getAppId());
                    }else {
                        String username = JwtUtil.getUsername(requestInfo.getToken());
                        adminLoginLog.setUsername(username);
                    }
                    // 保存登录日志
                    shopLoginLogService.save(adminLoginLog);
                }
            }
        } catch (Exception e) {
            log.error("保存系统登录日志失败", e);
        }
    }


    /**
     * 释放资源
     */
    protected void remove() {
        threadLocal.remove();
        requestInfoThreadLocal.remove();
        operationLogThreadLocal.remove();
        MDC.clear();
    }

}
