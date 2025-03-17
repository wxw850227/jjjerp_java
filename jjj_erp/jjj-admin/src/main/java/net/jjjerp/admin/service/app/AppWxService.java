package net.jjjerp.admin.service.app;

import net.jjjerp.common.entity.app.AppWx;
import net.jjjerp.framework.common.service.BaseService;

/**
 * 微信小程序记录表 服务类
 *
 * @author jjjerp
 * @since 2022-07-03
 */
public interface AppWxService extends BaseService<AppWx> {

    /**
     * 修改
     * @param wxappId
     * @param wxappSecret
     * @return
     */
    Boolean edit(String wxappId, String wxappSecret);
}
