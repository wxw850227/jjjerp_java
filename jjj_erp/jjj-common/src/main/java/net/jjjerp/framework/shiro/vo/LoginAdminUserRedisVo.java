

package net.jjjerp.framework.shiro.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.common.bean.ClientInfo;

/**
 * 登录用户Redis对象，后台使用
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginAdminUserRedisVo extends LoginAdminUserVo {

    private static final long serialVersionUID = -3858850188055605806L;

    /**
     * 包装后的盐值
     */
    private String salt;

    /**
     * 登录ip
     */
    private ClientInfo clientInfo;

}
