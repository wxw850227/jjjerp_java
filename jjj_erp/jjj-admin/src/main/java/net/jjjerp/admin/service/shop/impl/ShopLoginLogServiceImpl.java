package net.jjjerp.admin.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminLoginLog;
import net.jjjerp.common.mapper.shop.ShopLoginLogMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.param.shop.LoginLogPageParam;
import net.jjjerp.admin.service.shop.ShopLoginLogService;
import net.jjjerp.admin.vo.shop.LoginLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 管理员登录记录表 服务实现类
 * @author jjjerp
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopLoginLogServiceImpl extends BaseServiceImpl<ShopLoginLogMapper, AdminLoginLog> implements ShopLoginLogService {

    /**
     * 获取所有登陆记录
     * @param loginLogPageParam
     * @return
     */
    public Paging<LoginLogVo> getList(LoginLogPageParam loginLogPageParam){
        Page<AdminLoginLog> page = new PageInfo(loginLogPageParam);
        LambdaQueryWrapper<AdminLoginLog> wrapper = new LambdaQueryWrapper<AdminLoginLog>();
        if(StringUtils.isNotEmpty(loginLogPageParam.getUsername())){
            wrapper.like(AdminLoginLog::getUsername, loginLogPageParam.getUsername());
        }
        wrapper.orderByDesc(AdminLoginLog::getCreateTime);
        IPage<AdminLoginLog> iPage = this.page(page, wrapper);
        IPage<LoginLogVo> result = iPage.convert(item -> {
            LoginLogVo vo = new LoginLogVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        return new Paging(result);
    }


}
