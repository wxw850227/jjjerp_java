package net.jjjerp.common.entity.product;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 商品副单位表
 *
 * @author jjjerp
 * @since 2024-06-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_deputy_unit")
@ApiModel(value = "ProductDeputyUnit对象")
public class ProductDeputyUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("副单位ID")
    @TableId(value = "deputy_unit_id", type = IdType.AUTO)
    private Long deputyUnitId;

    @ApiModelProperty("名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty("副单位名称")
    private String deputyUnitName;

    @ApiModelProperty("基础单位ID")
    private Long basicUnitId;

    @ApiModelProperty("比例")
    private BigDecimal ratio;

    @ApiModelProperty("副单位层级")
    private Integer deputyNum;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
