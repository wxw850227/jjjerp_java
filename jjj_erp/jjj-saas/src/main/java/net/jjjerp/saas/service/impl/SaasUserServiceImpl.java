

package net.jjjerp.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.service.SaasUserService;
import net.jjjerp.saas.vo.LoginAdminUserTokenVo;
import net.jjjerp.common.entity.admin.SaasUser;
import net.jjjerp.common.mapper.admin.SaasUserMapper;
import net.jjjerp.config.constant.CommonRedisKey;
import net.jjjerp.config.properties.JwtProperties;
import net.jjjerp.config.properties.SpringBootJjjProperties;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.shiro.cache.AdminLoginRedisService;
import net.jjjerp.framework.shiro.jwt.JwtToken;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.framework.shiro.util.JwtUtil;
import net.jjjerp.framework.shiro.util.SaltUtil;
import net.jjjerp.framework.shiro.vo.LoginAdminUserVo;
import net.jjjerp.framework.util.PasswordUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class SaasUserServiceImpl extends BaseServiceImpl<SaasUserMapper, SaasUser> implements SaasUserService {

    @Autowired
    private SaasUserMapper saasUserMapper;

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
    private AdminLoginRedisService adminLoginRedisService;

    public LoginAdminUserTokenVo login(String username, String password){
        SaasUser saasUser = saasUserMapper.selectOne(new LambdaQueryWrapper<SaasUser>()
                .eq(SaasUser::getUserName, username));
        if(saasUser == null){
            throw new BusinessException("用户名或密码错误");
        }
        String encryptPassword = PasswordUtil.encrypt(password, saasUser.getSalt());
        if (!encryptPassword.equals(saasUser.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // 将系统用户对象转换成登录用户对象
        LoginAdminUserVo loginAdminUserVo = new LoginAdminUserVo();
        BeanUtils.copyProperties(saasUser, loginAdminUserVo);
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(saasUser.getSalt(), jwtProperties);

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
        adminLoginRedisService.cacheLoginInfo(jwtToken, loginAdminUserVo);
        log.debug("登录成功,username:{}", username);

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginAdminUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginAdminUserTokenVo loginAdminUserTokenVo = new LoginAdminUserTokenVo();
        loginAdminUserTokenVo.setToken(token);
        loginAdminUserTokenVo.setLoginAdminUserVo(loginAdminUserVo);
        return loginAdminUserTokenVo;
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public Boolean renew(String password){
        SaasUser saasUser = saasUserMapper.selectOne(new LambdaQueryWrapper<SaasUser>());

        String encryptPassword = PasswordUtil.encrypt(password, saasUser.getSalt());
        SaasUser updateBean = new SaasUser();
        updateBean.setSaasUserId(saasUser.getSaasUserId());
        updateBean.setPassword(encryptPassword);
        return this.updateById(updateBean);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request, "saas");
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
        redisTemplate.delete(String.format(CommonRedisKey.ADMIN_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.ADMIN_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.ADMIN_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.ADMIN_LOGIN_USER_TOKEN, username, tokenMd5));
    }
}
