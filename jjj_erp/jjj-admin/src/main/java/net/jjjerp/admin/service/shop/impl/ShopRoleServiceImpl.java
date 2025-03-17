package net.jjjerp.admin.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminRole;
import net.jjjerp.common.entity.shop.AdminRoleAccess;
import net.jjjerp.common.mapper.shop.ShopRoleMapper;
import net.jjjerp.admin.mapper.shop.ShopUserRoleMapper;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.param.shopUser.ShopRoleParam;
import net.jjjerp.admin.service.shop.ShopRoleAccessService;
import net.jjjerp.admin.service.shop.ShopRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家用户角色表 服务实现类
 * @author jjjerp
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopRoleServiceImpl extends BaseServiceImpl<ShopRoleMapper, AdminRole> implements ShopRoleService {
    @Autowired
    private ShopRoleAccessService shopRoleAccessService;
    @Autowired
    private ShopUserRoleMapper shopUserRoleMapper;

    /**
     * 角色列表
     * @param
     * @return
     */
    public List<AdminRole> getList(){
        return this.list(new LambdaQueryWrapper<AdminRole>().orderByAsc(AdminRole::getSort)
                .orderByAsc(AdminRole::getCreateTime));
    }
    /**
     * 新增角色
     * @param shopRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ShopRoleParam shopRoleParam){
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(shopRoleParam, role);
        // 保存主表
        this.save(role);
        // 保存权限
        this.saveAccess(shopRoleParam, role.getRoleId());
        return true;
    }
    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    public List<Integer> getSelectList(Integer roleId){
        List<AdminRoleAccess> accessList = shopRoleAccessService.list(new LambdaQueryWrapper<AdminRoleAccess>()
                        .eq(AdminRoleAccess::getRoleId, roleId));
        List<Integer> idList = new ArrayList<>();
        accessList.forEach(item ->{
            idList.add(item.getAccessId());
        });
        return idList;
    }

    /**
     * 编辑角色
     * @param shopRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(ShopRoleParam shopRoleParam){
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(shopRoleParam, role);
        // 保存主表
        this.updateById(role);
        // 先删除权限
        shopRoleAccessService.remove(new LambdaQueryWrapper<AdminRoleAccess>()
                .eq(AdminRoleAccess::getRoleId, shopRoleParam.getRoleId()));
        // 保存权限
        this.saveAccess(shopRoleParam, role.getRoleId());
        return true;
    }

    /**
     * 保存权限
     * @param shopRoleParam
     * @param roleId
     */
    private void saveAccess(ShopRoleParam shopRoleParam, Integer roleId){
        List<AdminRoleAccess> accessList = new ArrayList<>();
        shopRoleParam.getAccessId().forEach(item -> {
            AdminRoleAccess access = new AdminRoleAccess();
            access.setRoleId(roleId);
            access.setAccessId(item);
            accessList.add(access);
        });
        shopRoleAccessService.saveBatch(accessList);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer roleId){
        // 判断是否有用户引用该角色
        Integer count = shopUserRoleMapper.getUserRoleCount(roleId);
        if(count > 0){
            throw new BusinessException("当前角色下存在用户，不允许删除");
        }
        shopRoleAccessService.remove(new LambdaQueryWrapper<AdminRoleAccess>().eq(AdminRoleAccess::getRoleId, roleId));
        return this.removeById(roleId);
    }
}
