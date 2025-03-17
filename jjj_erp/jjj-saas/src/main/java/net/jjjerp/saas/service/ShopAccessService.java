package net.jjjerp.saas.service;

import net.jjjerp.saas.param.ShopAccessParam;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 商家用户权限表 服务类
 *
 * @author jjjerp
 * @since 2022-06-22
 */
public interface ShopAccessService extends BaseService<AdminAccess> {
    /**
     * 获取未挂载到插件分类下的插件
     * @return
     */
    List<AdminAccess> getAllPlus();

    /**
     * 清除插件分类,不在插件分类下显示
     * @param accessId
     * @return
     */
    Boolean clearPlusCategoryId(Integer accessId);

    Boolean editPlusCategoryId(Integer accessId, Integer plusCategoryId);

    Boolean editStatusById(Integer accessId, Integer status);

    /**
     * 真删除
     * @param accessId
     * @return
     */
    Boolean delById(Integer accessId);
    /**
     * 新增
     * @param shopAccessParam
     * @return
     */
    Boolean add(ShopAccessParam shopAccessParam);
    /**
     * 修改
     * @param shopAccessParam
     * @return
     */
    Boolean edit(ShopAccessParam shopAccessParam);
}
