package net.jjjerp.saas.service;

import net.jjjerp.saas.param.RegionPageParam;
import net.jjjerp.saas.param.RegionParam;
import net.jjjerp.saas.vo.RegionVo;
import net.jjjerp.common.entity.settings.Region;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

/**
 *  服务类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
public interface RegionService extends BaseService<Region> {
    /**
     * 地区列表
     * @param regionPageParam
     * @return
     */
    Paging<Region> getList(RegionPageParam regionPageParam);
    /**
     * 删除
     * @param id
     * @return
     */
    Boolean setDelete(Integer id);
    /**
     * 新增
     * @param regionParam
     * @return
     */
    Boolean add(RegionParam regionParam);

    /**
     * 获取编辑数据
     * @param id
     * @return
     */
    RegionVo getEditData(Integer id);
    /**
     * 修改
     * @param regionParam
     * @return
     */
    Boolean edit(RegionParam regionParam);
}
