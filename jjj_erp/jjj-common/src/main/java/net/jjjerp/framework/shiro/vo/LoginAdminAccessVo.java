

package net.jjjerp.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.shop.AdminAccess;

import java.util.List;

/**
 * 登录用户对象，响应给前端
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginAdminAccessVo extends AdminAccess {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty("子权限")
    private List<LoginAdminAccessVo> children;

}
