package net.jjjerp.saas.service;

import net.jjjerp.saas.param.AppPageParam;
import net.jjjerp.saas.param.AppParam;
import net.jjjerp.saas.vo.AppVo;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

/**
 * 微信小程序记录表 服务类
 *
 * @author jjjerp
 * @since 2022-06-23
 */
public interface AppService extends BaseService<App> {
    /**
     * app列表
     * @param appPageParam
     * @return
     */
    Paging<AppVo> getList(AppPageParam appPageParam);

    /**
     * 修改状态
     * @param appId
     * @return
     */
    Boolean editStatusById(Integer appId);
    /**
     * 修改微信服务商支付状态
     * @param appId
     * @return
     */
    Boolean updateWxStatus(Integer appId);
    /**
     * 删除
     * @param appId
     * @return
     */
    Boolean setDelete(Integer appId);
    /**
     * 新增
     * @param appParam
     * @return
     */
    Boolean add(AppParam appParam);
    /**
     * 修改
     * @param appParam
     * @return
     */
    Boolean edit(AppParam appParam);
}
