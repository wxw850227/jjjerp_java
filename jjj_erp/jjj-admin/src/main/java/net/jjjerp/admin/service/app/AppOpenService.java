package net.jjjerp.admin.service.app;

import net.jjjerp.common.entity.app.AppOpen;
import net.jjjerp.framework.common.service.BaseService;

/**
 * app应用记录表 服务类
 * @author jjjerp
 * @since 2022-07-27
 */
public interface AppOpenService extends BaseService<AppOpen> {

    /**
     * 修改
     * @param openappId
     * @param openappSecret
     * @return
     */
    Boolean edit(String openappId, String openappSecret, String logo);
}
