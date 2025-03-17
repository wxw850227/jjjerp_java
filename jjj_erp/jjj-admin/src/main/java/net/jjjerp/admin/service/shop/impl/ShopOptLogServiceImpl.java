package net.jjjerp.admin.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminOptLog;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.mapper.shop.ShopOptLogMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.param.shop.OptLogPageParam;
import net.jjjerp.admin.service.shop.ShopOptLogService;
import net.jjjerp.admin.service.shop.ShopUserService;
import net.jjjerp.admin.vo.shop.OptLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员操作记录表 服务实现类
 * @author jjjerp
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopOptLogServiceImpl extends BaseServiceImpl<ShopOptLogMapper, AdminOptLog> implements ShopOptLogService {

    @Autowired
    private ShopUserService shopUserService;

    /**
     * 获取所有操作记录
     * @param optLogPageParam
     * @return
     */
    public Paging<OptLogVo> getList(OptLogPageParam optLogPageParam){
        Page<AdminOptLog> page = new PageInfo(optLogPageParam);
        LambdaQueryWrapper<AdminOptLog> wrapper = new LambdaQueryWrapper<AdminOptLog>();
        if(StringUtils.isNotEmpty(optLogPageParam.getUsername())){
            List<Integer> userIds = shopUserService.list(new LambdaQueryWrapper<AdminUser>()
                            .and(i -> i.or().like(AdminUser::getUserName, optLogPageParam.getUsername())
                                    .or().like(AdminUser::getRealName, optLogPageParam.getUsername())))
                    .stream().map(AdminUser::getShopUserId).collect(Collectors.toList());
            if(userIds.size() > 0){
                //模糊搜索
                wrapper.in(AdminOptLog::getShopUserId, userIds);
            }else {
                //没有匹配到
                wrapper.eq(AdminOptLog::getShopUserId, -1);
            }
        }
        wrapper.orderByDesc(AdminOptLog::getCreateTime);
        IPage<AdminOptLog> iPage = this.page(page, wrapper);
        IPage<OptLogVo> result = iPage.convert(item -> {
            OptLogVo vo = new OptLogVo();
            BeanUtils.copyProperties(item, vo);
            AdminUser adminUser = shopUserService.getById(vo.getShopUserId());
            vo.setRealName(adminUser.getRealName());
            vo.setUserName(adminUser.getUserName());
            return vo;
        });
        return new Paging(result);
    }
}
