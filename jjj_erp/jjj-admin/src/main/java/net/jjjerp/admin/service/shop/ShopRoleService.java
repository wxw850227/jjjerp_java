package net.jjjerp.admin.service.shop;

import net.jjjerp.common.entity.shop.AdminRole;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.admin.param.shopUser.ShopRoleParam;

import java.util.List;

/**
 * 商家用户角色表 服务类
 * @author jjjerp
 * @since 2022-07-09
 */
public interface ShopRoleService extends BaseService<AdminRole> {
    /**
     * 角色列表
     * @return
     */
    List<AdminRole> getList();

    /**
     * 新增角色
     * @param shopRoleParam
     * @return
     */
    Boolean add(ShopRoleParam shopRoleParam);

    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    List<Integer> getSelectList(Integer roleId);

    /**
     * 编辑角色
     * @param shopRoleParam
     * @return
     */
    Boolean edit(ShopRoleParam shopRoleParam);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Boolean delete(Integer roleId);
}
