package net.jjjerp.common.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信公众号记录表
 *
 * @author jjjerp
 * @since 2022-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_app_mp")
@ApiModel(value = "AppMp对象")
public class AppMp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("appid")
    @TableId(value = "app_id", type = IdType.AUTO)
    private Integer appId;

    @ApiModelProperty("公众号AppID")
    private String mpappId;

    @ApiModelProperty("公众号AppSecret")
    private String mpappSecret;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
