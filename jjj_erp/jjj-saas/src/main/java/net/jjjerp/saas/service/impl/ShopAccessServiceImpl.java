package net.jjjerp.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.ShopAccessParam;
import net.jjjerp.saas.service.ShopAccessService;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.mapper.shop.ShopAccessMapper;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商家用户权限表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-22
 */
@Slf4j
@Service
public class ShopAccessServiceImpl extends BaseServiceImpl<ShopAccessMapper, AdminAccess> implements ShopAccessService {
    @Autowired
    private ShopAccessMapper shopAccessMapper;

    public List<AdminAccess> getAllPlus(){
        // 查找插件根节点
        LambdaQueryWrapper<AdminAccess> plusWrapper = Wrappers.lambdaQuery();
        plusWrapper.eq(AdminAccess::getPath, "/plus/plus/index");
        AdminAccess plusBean = this.getOne(plusWrapper);
        // 查找插件
        LambdaQueryWrapper<AdminAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AdminAccess::getParentId, plusBean.getAccessId());
        wrapper.eq(AdminAccess::getPlusCategoryId, 0);
        // 获取所有已启用的权限列表
        return this.list(wrapper);
    }


    public Boolean clearPlusCategoryId(Integer accessId){
        AdminAccess access = new AdminAccess();
        access.setAccessId(accessId);
        access.setPlusCategoryId(0);
        return this.updateById(access);
    }

    public Boolean editPlusCategoryId(Integer accessId, Integer plusCategoryId){
        AdminAccess access = new AdminAccess();
        access.setAccessId(accessId);
        access.setPlusCategoryId(plusCategoryId);
        return this.updateById(access);
    }

    public Boolean editStatusById(Integer accessId, Integer status){
        AdminAccess access = new AdminAccess();
        access.setAccessId(accessId);
        access.setIsShow(status);
        return this.updateById(access);
    }

    /**
     * 真删除
     * @param accessId
     * @return
     */
    public Boolean delById(Integer accessId){
        // 是否存在子菜单
        LambdaQueryWrapper<AdminAccess> countWrapper = Wrappers.lambdaQuery();
        countWrapper.eq(AdminAccess::getParentId, accessId);
        if(this.count(countWrapper) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        return this.removeById(accessId);
    }

    /**
     * 新增
     * @param shopAccessParam
     * @return
     */
    public Boolean add(ShopAccessParam shopAccessParam){
        LambdaQueryWrapper<AdminAccess> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminAccess::getPath,shopAccessParam.getPath());
        if(this.count(wrapper) > 0){
            throw new BusinessException("路径已存在，请更改后再试");
        }
        AdminAccess access = new AdminAccess();
        BeanUtils.copyProperties(shopAccessParam, access);
        // 11位时间戳
        String timestamp = String.valueOf((new Date()).getTime()/1000);
        access.setAccessId(Integer.valueOf(timestamp));
        return this.save(access);
    }

    /**
     * 修改
     * @param shopAccessParam
     * @return
     */
    public Boolean edit(ShopAccessParam shopAccessParam){
        AdminAccess access = new AdminAccess();
        BeanUtils.copyProperties(shopAccessParam, access);
        return this.updateById(access);
    }
}
