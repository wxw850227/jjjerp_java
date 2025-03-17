

package net.jjjerp.admin.service.shop.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.entity.shop.AdminUserRole;
import net.jjjerp.admin.mapper.shop.ShopUserRoleMapper;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.config.constant.CommonRedisKey;
import net.jjjerp.config.properties.JwtProperties;
import net.jjjerp.config.properties.SpringBootJjjProperties;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.shiro.cache.ShopLoginRedisService;
import net.jjjerp.framework.shiro.jwt.JwtToken;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.framework.shiro.util.JwtUtil;
import net.jjjerp.framework.shiro.util.SaltUtil;
import net.jjjerp.framework.shiro.vo.LoginAdminAccessVo;
import net.jjjerp.framework.shiro.vo.LoginShopUserTokenVo;
import net.jjjerp.framework.shiro.vo.LoginShopUserVo;
import net.jjjerp.framework.util.PasswordUtil;
import net.jjjerp.framework.util.ShopLoginUtil;
import net.jjjerp.admin.mapper.shop.ShopUserMapper;
import net.jjjerp.admin.param.shopUser.ShopUserPageParam;
import net.jjjerp.admin.param.shopUser.ShopUserParam;
import net.jjjerp.admin.service.app.AppService;
import net.jjjerp.admin.service.shop.ShopAccessService;
import net.jjjerp.admin.service.shop.ShopUserRoleService;
import net.jjjerp.admin.service.shop.ShopUserService;
import net.jjjerp.admin.vo.shopUser.ShopUserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class ShopUserServiceImpl extends BaseServiceImpl<ShopUserMapper, AdminUser> implements ShopUserService {

    @Autowired
    private ShopUserMapper shopUserMapper;
    @Autowired
    private ShopUserRoleMapper shopUserRoleMapper;
    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Lazy
    @Autowired
    private JwtProperties jwtProperties;
    @Lazy
    @Autowired
    private RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    private ShopLoginRedisService shopLoginRedisService;
    @Autowired
    private ShopAccessService shopAccessService;
    @Autowired
    private ShopUserRoleService shopUserRoleService;
    @Autowired
    private AppService appService;

    //业务员列表
    @Override
    public List<AdminUser> getSales() {
        return this.list(new LambdaQueryWrapper<AdminUser>()
                //0=显示1=伪删除
                .eq(AdminUser::getIsDelete,0)
                //是否为业务员0不是,1是
                .eq(AdminUser::getIsSales,1)
        );
    }

    //系统用户列表
    @Override
    public List<AdminUser> getAll() {
        return this.list(new LambdaQueryWrapper<AdminUser>()
                //0=显示1=伪删除
                .eq(AdminUser::getIsDelete,0)
        );
    }

    @InterceptorIgnore(tenantLine = "true")
    public LoginShopUserTokenVo login(String username, String password){
        AdminUser adminUser = this.getOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getUserName, username)
                //0=显示1=伪删除
                .eq(AdminUser::getIsDelete,0)
                .comment(CommonConstant.NOT_WITH_App_Id));
        if(adminUser == null){
            throw new AuthenticationException("用户名或密码错误");
        }
        String encryptPassword = PasswordUtil.encrypt(password, adminUser.getSalt());
        if (!encryptPassword.equals(adminUser.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // 检查应用
        App app = appService.getById(adminUser.getAppId());
        if(app == null){
            throw new AuthenticationException("登录失败, 未找到应用信息");
        }
        if(app.getIsRecycle()){
            throw new AuthenticationException("登录失败, 当前应用已禁用");
        }
        if(app.getIsDelete()){
            throw new AuthenticationException("登录失败, 当前应用已删除");
        }
        if (app.getExpireTime() != null && app.getExpireTime().before(new Date())) {
            throw new AuthenticationException("登录失败, 当前应用已过期，请联系平台续费");
        }
        // 将系统用户对象转换成登录用户对象
        LoginShopUserVo loginShopUserVo = new LoginShopUserVo();
        BeanUtils.copyProperties(adminUser, loginShopUserVo);
        // 获取用户权限集合
        // 1.如果是超管，则有所有权限
        List<AdminAccess> accessList = null;
        if(adminUser.getIsSuper()){
            accessList = shopAccessService.list(new LambdaQueryWrapper<AdminAccess>()
                    .eq(AdminAccess::getIsShow, true)
                    .orderByAsc(AdminAccess::getSort).orderByAsc(AdminAccess::getCreateTime)
                    .comment(CommonConstant.NOT_WITH_App_Id));
        }else{
            accessList = shopUserMapper.getAccessByUserId(adminUser.getShopUserId());
            if (CollectionUtils.isEmpty(accessList)) {
                throw new AuthenticationException("权限列表不能为空");
            }
        }
        loginShopUserVo.setPermissions(this.transPermissions(accessList));
        loginShopUserVo.setMenus(this.transMenus(accessList));
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(adminUser.getSalt(), jwtProperties);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken(username, newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, newSalt, expireSecond);

        boolean enableShiro = springBootJjjProperties.getShiro().isEnable();
        if (enableShiro) {
            // 从SecurityUtils里边创建一个 subject
            Subject subject = SecurityUtils.getSubject();
            // 执行认证登录
            subject.login(jwtToken);
        } else {
            log.warn("未启用Shiro");
        }

        // 缓存登录信息到Redis
        shopLoginRedisService.cacheLoginInfo(jwtToken, loginShopUserVo);
        log.debug("登录成功,username:{}", username);

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginShopUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginShopUserTokenVo loginShopUserTokenVo = new LoginShopUserTokenVo();
        loginShopUserTokenVo.setToken(token);
        loginShopUserTokenVo.setLoginShopUserVo(loginShopUserVo);
        return loginShopUserTokenVo;
    }

    /**
     * 后端权限集合
     * @param accessList
     * @return
     */
    private Set<String> transPermissions(List<AdminAccess> accessList){
        Set<String> set = new HashSet<String>();
        accessList.forEach(item -> {
            set.add(item.getPath());
        });
        return set;
    }

    /**
     * 前端权限菜单集合
     * @param accessList
     * @return
     */
    private List<LoginAdminAccessVo> transMenus(List<AdminAccess> accessList){
        // 转成vo
        List<LoginAdminAccessVo> voList = accessList.stream().map(e -> {
                LoginAdminAccessVo shopAccessVo = new LoginAdminAccessVo();
                BeanUtils.copyProperties(e, shopAccessVo);
                return shopAccessVo;
        }).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(LoginAdminAccessVo::getAccessId))), ArrayList::new));
        voList.sort(Comparator.comparing(LoginAdminAccessVo::getSort));
        // 遍历成树形结构
        List<LoginAdminAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        collect.sort(Comparator.comparing(LoginAdminAccessVo::getSort));
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<LoginAdminAccessVo> getChildList(AdminAccess bean, List<LoginAdminAccessVo> voList) {
        return voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getAccessId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public Boolean renew(String oldpass, String password, String confirmPass){
        // 当前登录用户
        AdminUser adminUser = this.getById(ShopLoginUtil.getUserId());
        if(!password.equals(confirmPass)){
            throw new BusinessException("两次密码不相同");
        }
        String encryptOldPassword = PasswordUtil.encrypt(oldpass, adminUser.getSalt());
        if(!encryptOldPassword.equals(adminUser.getPassword())){
            throw new BusinessException("原密码不正确");
        }
        String encryptPassword = PasswordUtil.encrypt(password, adminUser.getSalt());
        AdminUser updateBean = new AdminUser();
        updateBean.setShopUserId(adminUser.getShopUserId());
        updateBean.setPassword(encryptPassword);
        return this.updateById(updateBean);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request, "admin");
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }

    private void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(String.format(CommonRedisKey.SHOP_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.SHOP_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.SHOP_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.SHOP_LOGIN_USER_TOKEN, username, tokenMd5));
    }

    /**
     * 列表
     * @param params
     * @throws Exception
     */
    public Paging<ShopUserVo> getList(ShopUserPageParam params){
        Page<AdminUser> page = new PageInfo<>(params);
        IPage<AdminUser> iPage = this.page(page, new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getIsDelete, false));
        // 最终返回分页对象
        IPage<ShopUserVo> resultPage = iPage.convert(item -> {
            ShopUserVo vo = new ShopUserVo();
            BeanUtil.copyProperties(item, vo);
            // 角色名称
            if(!vo.getIsSuper()){
                vo.setRoleList(shopUserRoleMapper.getRoleListByUser(vo.getShopUserId()));
            }
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 新增用户
     * @param shopUserParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ShopUserParam shopUserParam){
        //验证账号唯一性
        int count = this.count(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getIsDelete,0)
                .comment(CommonConstant.NOT_WITH_App_Id)
                .eq(AdminUser::getUserName,shopUserParam.getUserName()));
        if(count > 0){
            throw new BusinessException("该用户名已存在，请更改后再试");
        }
        AdminUser user = new AdminUser();
        user.setUserName(shopUserParam.getUserName());
        user.setRealName(shopUserParam.getRealName());
        user.setIsSales(shopUserParam.getIsSales());
        // 盐值
        String salt = SaltUtil.generateSalt();
        // 密码加密
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encrypt(shopUserParam.getPassword(), salt));
        this.save(user);
        // 保存角色
        this.saveRole(shopUserParam, user.getShopUserId());
        return true;
    }

    /**
     * 编辑用户
     * @param shopUserParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(ShopUserParam shopUserParam){
        AdminUser oldUser = this.getById(shopUserParam.getShopUserId());
        //如果修改了账号，则验证账号唯一性
        if(!oldUser.getUserName().equals(shopUserParam.getUserName())){
            //验证账号唯一性
            int count = this.count(new LambdaQueryWrapper<AdminUser>()
                    .eq(AdminUser::getIsDelete,0)
                    .comment(CommonConstant.NOT_WITH_App_Id)
                    .eq(AdminUser::getUserName,shopUserParam.getUserName()));
            if(count > 0){
                throw new BusinessException("该用户名已存在，请更改后再试");
            }
        }
        AdminUser user = new AdminUser();
        user.setShopUserId(shopUserParam.getShopUserId());
        user.setUserName(shopUserParam.getUserName());
        user.setRealName(shopUserParam.getRealName());
        user.setIsSales(shopUserParam.getIsSales());
        if(StringUtils.isNotEmpty(shopUserParam.getPassword())){
            // 盐值
            String salt = SaltUtil.generateSalt();
            // 密码加密
            user.setSalt(salt);
            user.setPassword(PasswordUtil.encrypt(shopUserParam.getPassword(), salt));
        }
        this.updateById(user);
        // 先删除角色
        shopUserRoleService.remove(new LambdaQueryWrapper<AdminUserRole>()
                .eq(AdminUserRole::getShopUserId, shopUserParam.getShopUserId()));
        // 保存角色
        this.saveRole(shopUserParam, shopUserParam.getShopUserId());
        return true;
    }

    private void saveRole(ShopUserParam shopUserParam, Integer shopUserId){
        List<AdminUserRole> roleList = new ArrayList<>();
        shopUserParam.getRoleId().forEach(item -> {
            AdminUserRole role = new AdminUserRole();
            role.setRoleId(item);
            role.setShopUserId(shopUserId);
            roleList.add(role);
        });
        shopUserRoleService.saveBatch(roleList);
    }

    /**
     * 删除用户
     * @param shopUserId
     * @return
     */
    public Boolean setDelete(Integer shopUserId){
        return this.update(new LambdaUpdateWrapper<AdminUser>()
                .eq(AdminUser::getShopUserId, shopUserId).set(AdminUser::getIsDelete, true));
    }
}
