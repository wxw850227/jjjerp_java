

package net.jjjerp.framework.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.service.app.AppService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 自定义权限拦截器
 */
@Slf4j
public class FrontInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AppService appService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        App app = appService.getById(RequestDetailThreadLocal.getRequestDetail().getAppId());
        if (app == null) {
            throw new BusinessException("当前应用信息不存在");
        }
        if (app.getIsRecycle() || app.getIsDelete()) {
            throw new BusinessException("当前应用已删除");
        }
        if (app.getExpireTime() != null && app.getExpireTime().before(new Date())) {
            throw new BusinessException("当前应用已过期");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

}