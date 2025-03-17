

package net.jjjerp.framework.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.config.constant.CommonRedisKey;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.framework.shiro.util.JwtUtil;
import net.jjjerp.framework.shiro.vo.LoginAdminUserRedisVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 获取登录信息工具类
 */
@Slf4j
@Component
public class AdminLoginUtil {

    private static RedisTemplate redisTemplate;

    public AdminLoginUtil(RedisTemplate redisTemplate) {
        AdminLoginUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    public static LoginAdminUserRedisVo getLoginAdminUserRedisVo() {
        // 获取当前登录用户
        String token = JwtTokenUtil.getToken("saas");
        String username = JwtUtil.getUsername(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return (LoginAdminUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.ADMIN_LOGIN_USER, username));
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static Long getUserId() {
        LoginAdminUserRedisVo loginAdminUserRedisVo = getLoginAdminUserRedisVo();
        if (loginAdminUserRedisVo == null) {
            return null;
        }
        return loginAdminUserRedisVo.getSaasUserId();
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public static String getUsername() {
        LoginAdminUserRedisVo loginAdminUserRedisVo = getLoginAdminUserRedisVo();
        if (loginAdminUserRedisVo == null) {
            return null;
        }
        return loginAdminUserRedisVo.getUserName();
    }

}
