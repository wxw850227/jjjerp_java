

package net.jjjerp.framework.log.service;

import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.entity.SysLoginLog;
import net.jjjerp.framework.log.param.SysLoginLogPageParam;

/**
 * 系统登录日志 服务类
 */
public interface SysLoginLogService extends BaseService<SysLoginLog> {

    /**
     * 保存
     *
     * @param sysLoginLog
     * @return
     * @throws Exception
     */
    boolean saveSysLoginLog(SysLoginLog sysLoginLog) throws Exception;

    /**
     * 修改
     *
     * @param sysLoginLog
     * @return
     * @throws Exception
     */
    boolean updateSysLoginLog(SysLoginLog sysLoginLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysLoginLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param sysLoginLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<SysLoginLog> getSysLoginLogPageList(SysLoginLogPageParam sysLoginLogPageParam) throws Exception;

}
