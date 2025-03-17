package net.jjjerp.common.entity.settings;

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
 * 电子面单设置主表
 *
 * @author jjjerp
 * @since 2023-12-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_label_setting")
@ApiModel(value = "LabelSetting对象")
public class LabelSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电子面单设置id")
    @TableId(value = "label_setting_id", type = IdType.AUTO)
    private Integer labelSettingId;

    @ApiModelProperty("电子面单设置名称")
    private String settingName;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("电子面单客户账户或月结账号，需贵司向当地快递公司网点申请")
    private String partnerId;

    @ApiModelProperty("电子面单密码")
    private String partnerKey;

    @ApiModelProperty("电子面单密钥")
    private String partnerSecret;

    @ApiModelProperty("电子面单客户账户名称")
    private String partnerName;

    @ApiModelProperty("收件网点名称")
    private String net;

    @ApiModelProperty("电子面单承载编号")
    private String code;

    @ApiModelProperty("电子面单承载快递员名")
    private String checkMan;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("小程序d")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
