package net.jjjerp.common.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.service.shop.ShopAccessService;
import net.jjjerp.common.vo.shop.AdminAccessVo;
import net.jjjerp.config.constant.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ShopAccessUtils {
    @Autowired
    private ShopAccessService shopAccessService;

    public List<AdminAccessVo> getAll(){
        LambdaQueryWrapper<AdminAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(AdminAccess::getSort).orderByAsc(AdminAccess::getCreateTime).comment(CommonConstant.NOT_WITH_App_Id);
        // 获取所有权限列表
        List<AdminAccess> list = shopAccessService.list(wrapper);
        // 转成vo
        List<AdminAccessVo> voList = list.stream().map(e -> {
            AdminAccessVo shopAccessVo = new AdminAccessVo();
            BeanUtils.copyProperties(e, shopAccessVo);
            return shopAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<AdminAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    public List<AdminAccessVo> getShopAll(){
        LambdaQueryWrapper<AdminAccess> wrapper = Wrappers.lambdaQuery();
        //是否显示1=显示0=不显示
        wrapper.eq(AdminAccess::getIsShow,1);
        wrapper.orderByAsc(AdminAccess::getSort).orderByAsc(AdminAccess::getCreateTime).comment(CommonConstant.NOT_WITH_App_Id);
        // 获取所有权限列表
        List<AdminAccess> list = shopAccessService.list(wrapper);
        // 转成vo
        List<AdminAccessVo> voList = list.stream().map(e -> {
            AdminAccessVo shopAccessVo = new AdminAccessVo();
            BeanUtils.copyProperties(e, shopAccessVo);
            return shopAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<AdminAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<AdminAccessVo> getChildList(AdminAccess bean, List<AdminAccessVo> voList) {
        List<AdminAccessVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getAccessId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }
}
