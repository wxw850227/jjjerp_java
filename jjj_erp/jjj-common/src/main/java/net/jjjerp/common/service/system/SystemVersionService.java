package net.jjjerp.common.service.system;

import net.jjjerp.common.entity.system.SystemVersion;
import net.jjjerp.framework.common.service.BaseService;

/**
 * 系统信息表 服务类
 *
 * @author jjjerp
 * @since 2022-06-22
 */
public interface SystemVersionService extends BaseService<SystemVersion> {
    public String getVersion();
}
