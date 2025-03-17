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
 * 电子面单模板主表
 *
 * @author jjjerp
 * @since 2023-12-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_delivery_template")
@ApiModel(value = "DeliveryTemplate对象")
public class DeliveryTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电子面单模板id")
    @TableId(value = "template_id", type = IdType.AUTO)
    private Integer templateId;

    @ApiModelProperty("电子面单模板编码")
    private String templateNum;

    @ApiModelProperty("电子面单模板名称")
    private String templateName;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("小程序d")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
