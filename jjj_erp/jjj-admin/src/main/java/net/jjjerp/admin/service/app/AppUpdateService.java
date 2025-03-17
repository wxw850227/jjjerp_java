package net.jjjerp.admin.service.app;

import net.jjjerp.common.entity.app.AppUpdate;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.param.app.AppUpdateParam;

/**
 * app升级记录表 服务类
 * @author jjjerp
 * @since 2022-07-27
 */
public interface AppUpdateService extends BaseService<AppUpdate> {

    /**
     * 分页查询
     * @param commonPageParam
     * @return
     */
    Paging<AppUpdate> getList(CommonPageParam commonPageParam);

    /**
     * 添加
     * @param appUpdateParam
     * @return
     */
    boolean add(AppUpdateParam appUpdateParam);

    /**
     * 修改
     * @param appUpdateParam
     * @return
     */
    boolean edit(AppUpdateParam appUpdateParam);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delById(Integer id);
}
