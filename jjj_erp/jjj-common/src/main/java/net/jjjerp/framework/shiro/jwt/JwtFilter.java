

package net.jjjerp.framework.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.config.properties.JwtProperties;
import net.jjjerp.framework.common.api.ApiCode;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.shiro.cache.AdminLoginRedisService;
import net.jjjerp.framework.shiro.cache.ShopLoginRedisService;
import net.jjjerp.framework.shiro.service.ShiroLoginService;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.framework.shiro.util.JwtUtil;
import net.jjjerp.framework.util.HttpServletResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shiro JWT授权过滤器
 **/
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    private ShiroLoginService shiroLoginService;

    private AdminLoginRedisService adminLoginRedisService;

    private ShopLoginRedisService shopLoginRedisService;

    private JwtProperties jwtProperties;

    public JwtFilter(ShiroLoginService shiroLoginService, AdminLoginRedisService adminLoginRedisService,
                     ShopLoginRedisService shopLoginRedisService, JwtProperties jwtProperties) {
        this.shiroLoginService = shiroLoginService;
        this.adminLoginRedisService = adminLoginRedisService;
        this.shopLoginRedisService = shopLoginRedisService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 将JWT Token包装成AuthenticationToken
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String url = ((ShiroHttpServletRequest) servletRequest).getRequestURI();
        String model = JwtTokenUtil.getModel(url);
        String token = JwtTokenUtil.getToken(model);
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("token不能为空");
        }
        if (JwtUtil.isExpired(token)) {
            throw new AuthenticationException("JWT Token已过期,token:" + token);
        }

        // 如果开启redis二次校验，或者设置为单个用户token登录，则先在redis中判断token是否存在
        if (jwtProperties.isRedisCheck() || jwtProperties.isSingleLogin()) {
            boolean redisExpired = false;
            if(url.startsWith("/api/saas/")){
                redisExpired = adminLoginRedisService.exists(token);
            }else if(url.startsWith("/api/admin/")){
                redisExpired = shopLoginRedisService.exists(token);
            }
            if (!redisExpired) {
                throw new AuthenticationException("Redis Token不存在,token:" + token);
            }
        }

        String username = JwtUtil.getUsername(token);
        String salt = null;
        if (jwtProperties.isSaltCheck()) {
            if(url.startsWith("/api/saas/")) {
                salt = adminLoginRedisService.getSalt(username);
            }else if(url.startsWith("/api/admin/")){
                salt = shopLoginRedisService.getSalt(username);
            }
        } else {
            salt = jwtProperties.getSecret();
        }
        return JwtToken.build(token, username, salt, jwtProperties.getExpireSecond());
    }

    /**
     * 访问失败处理
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 返回-1未登录
        String url = httpServletRequest.getRequestURI();
        log.error("onAccessDenied url：{}", url);
        ApiResult<Boolean> apiResult = ApiResult.fail(ApiCode.NOT_LOGIN);
        HttpServletResponseUtil.printJson(httpServletResponse, apiResult);
        return false;
    }

    /**
     * 判断是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("isAccessAllowed url:{}", url);
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) { //not found any token
            log.error("Token不能为空", e);
        } catch (Exception e) {
            log.error("访问错误", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 登录成功处理
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String url = WebUtils.toHttp(request).getRequestURI();
        log.debug("鉴权成功,token:{},url:{}", token, url);
        // 刷新token
        JwtToken jwtToken = (JwtToken) token;
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        shiroLoginService.refreshToken(jwtToken, url, httpServletResponse);
        return true;
    }

    /**
     * 登录失败处理
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("登录失败，token:" + token + ",error:" + e.getMessage(), e);
        return false;
    }
}
