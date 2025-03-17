package net.jjjerp.saas.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.AppPageParam;
import net.jjjerp.saas.param.AppParam;
import net.jjjerp.saas.service.AppService;
import net.jjjerp.saas.service.ShopUserService;
import net.jjjerp.saas.vo.AppVo;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.mapper.app.AppMapper;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.shiro.util.SaltUtil;
import net.jjjerp.framework.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {
    @Autowired
    private ShopUserService shopUserService;
    /**
     * app列表
     * @param appPageParam
     * @return
     */
    public Paging<AppVo> getList(AppPageParam appPageParam){
        Page<App> page = new PageInfo<>(appPageParam, OrderItem.asc(getLambdaColumn(App::getCreateTime)));
        IPage<App> iPage = this.page(page, new LambdaUpdateWrapper<App>().eq(App::getIsDelete, false));
        // 最终返回分页对象
        IPage<AppVo> resultPage = iPage.convert(result -> {
            AppVo vo = new AppVo();
            BeanUtil.copyProperties(result, vo);
            vo.setUserName(shopUserService.getOne(new LambdaQueryWrapper<AdminUser>()
                    .eq(AdminUser::getAppId, vo.getAppId()).eq(AdminUser::getIsSuper, true)).getUserName());
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 修改状态
     * @param appId
     * @return
     */
    public Boolean editStatusById(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsRecycle(app.getIsRecycle()?false:true);
        return this.updateById(updateBean);
    }

    /**
     * 修改微信服务商支付状态
     * @param appId
     * @return
     */
    public Boolean updateWxStatus(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setWeixinService(app.getWeixinService()?false:true);
        return this.updateById(updateBean);
    }

    /**
     * 删除
     * @param appId
     * @return
     */
    public Boolean setDelete(Integer appId){
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsDelete(true);
        return this.updateById(updateBean);
    }

    /**
     * 新增
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(AppParam appParam){
        // 是否存在用户名
        if(shopUserService.count(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getUserName, appParam.getUserName())) > 0){
            throw new BusinessException("商家用户名已存在");
        }
        // 保存主表
        App app = new App();
        BeanUtils.copyProperties(appParam, app);
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.save(app);
        // 保存管理员用户表
        AdminUser user = new AdminUser();
        user.setAppId(app.getAppId());
        user.setUserName(appParam.getUserName());
        user.setIsSuper(true);
        String salt = SaltUtil.generateSalt();
        // 密码加密
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encrypt(appParam.getPassword(), salt));
        return shopUserService.save(user);
    }

    /**
     * 修改
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(AppParam appParam){
        // 更新主表
        App app = new App();
        app.setAppId(appParam.getAppId());
        app.setWeixinService(appParam.getWeixinService());
        app.setAppName(appParam.getAppName());
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.updateById(app);
        // 更新管理员表
        AdminUser user = shopUserService.getOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getAppId, app.getAppId())
                .eq(AdminUser::getIsSuper, true));
        AdminUser updateUser = new AdminUser();
        if (!StringUtils.isEmpty(appParam.getPassword())) {
            if(StringUtils.isEmpty(user.getSalt())){
                String salt = SaltUtil.generateSalt();
                user.setSalt(salt);
            }
            updateUser.setPassword(PasswordUtil.encrypt(appParam.getPassword(), user.getSalt()));
        }
        if(!appParam.getUserName().equals(user.getUserName())){
            if(shopUserService.count(new LambdaQueryWrapper<AdminUser>()
                    .eq(AdminUser::getUserName, appParam.getUserName())) > 0){
                throw new BusinessException("商家用户名已存在");
            }
        }
        updateUser.setShopUserId(user.getShopUserId());
        updateUser.setUserName(appParam.getUserName());
        return shopUserService.updateById(updateUser);
    }
}
