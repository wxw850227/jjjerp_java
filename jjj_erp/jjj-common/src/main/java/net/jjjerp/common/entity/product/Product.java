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
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 产品表
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product")
@ApiModel(value = "Product对象")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    @ApiModelProperty("产品类型id")
    @NotNull(message = "商品分类不能为空")
    private Integer categoryId;

    @ApiModelProperty("名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty("品牌ID")
    private Integer mfrs;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("计量单位Id")
    @NotNull(message = "计量单位不能为空")
    private Long unitId;

    @ApiModelProperty("保质期天数")
    private Integer expiryNum;

    @ApiModelProperty("基础重量(kg)")
    private BigDecimal weight;

    @ApiModelProperty("基础体积(立方米)")
    private BigDecimal volume;

    @ApiModelProperty("长(米)")
    private BigDecimal grow;

    @ApiModelProperty("宽(米)")
    private BigDecimal width;

    @ApiModelProperty("高(米)")
    private BigDecimal height;

    @ApiModelProperty("启用 0-禁用  1-启用")
    private Integer enabled;

    @ApiModelProperty("商品条码")
    private String barCode;

    @ApiModelProperty("商品编码")
    @NotBlank(message = "商品编码不能为空", groups = {Update.class})
    private String productCode;

    @ApiModelProperty("货号")
    private String accountNum;

    @ApiModelProperty("仓位货架")
    private String position;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
