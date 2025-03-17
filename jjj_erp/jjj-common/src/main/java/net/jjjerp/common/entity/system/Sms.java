package net.jjjerp.common.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 用户注册短信表
 *
 * @author jjjerp
 * @since 2022-09-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_sms")
@ApiModel(value = "Sms对象")
public class Sms implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "sms_id", type = IdType.AUTO)
    private Integer smsId;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
