package net.jjjerp.admin.service.shop;

import net.jjjerp.common.entity.shop.AdminLoginLog;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.param.shop.LoginLogPageParam;
import net.jjjerp.admin.vo.shop.LoginLogVo;

/**
 * 管理员登录记录表 服务类
 * @author jjjerp
 * @since 2022-08-15
 */
public interface ShopLoginLogService extends BaseService<AdminLoginLog> {

    /**
     * 获取所有登陆记录
     * @param loginLogPageParam
     * @return
     */
    Paging<LoginLogVo> getList(LoginLogPageParam loginLogPageParam);

}
