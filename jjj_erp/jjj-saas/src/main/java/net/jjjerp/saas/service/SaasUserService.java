

package net.jjjerp.saas.service;

import net.jjjerp.saas.vo.LoginAdminUserTokenVo;
import net.jjjerp.common.entity.admin.SaasUser;
import net.jjjerp.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户 服务类
 */
public interface SaasUserService extends BaseService<SaasUser> {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    LoginAdminUserTokenVo login(String username, String password);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String password);
    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;
}
