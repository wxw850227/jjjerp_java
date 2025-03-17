package net.jjjerp.common.entity.product;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 产品价格扩展
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_extend")
@ApiModel(value = "ProductExtend对象")
public class ProductExtend implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "product_extend_id", type = IdType.AUTO)
    private Long productExtendId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("图片url")
    private String imageUrl;

    @ApiModelProperty("规格条码")
    private String barCode;

    @ApiModelProperty("规格编码")
    @NotBlank(message = "规格编码不能为空")
    private String productCode;

    @ApiModelProperty("多属性")
    private String sku;

    @ApiModelProperty("进价")
    private BigDecimal purchaseDecimal;

    @ApiModelProperty("吊牌价")
    private BigDecimal commodityDecimal;

    @ApiModelProperty("标准售价")
    private BigDecimal wholesaleDecimal;

    @ApiModelProperty("批发价")
    private BigDecimal lowDecimal;

    @ApiModelProperty("是否为默认单位，1是，0否")
    private Integer defaultFlag;

    @ApiModelProperty("实际总库存")
    private Long totalStock;

    @ApiModelProperty("锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量")
    private Long freezeStock;

    @ApiModelProperty("当前可用库存(实际库存-锁定库存)")
    private Long stockNum;

    @ApiModelProperty("期初库存")
    private Long openningStock;

    @ApiModelProperty("成本价,初始默认为0")
    private BigDecimal costPrice;

    @ApiModelProperty("是否可以修改期初成本价,0否,1是")
    private Integer initialStatus;

    @ApiModelProperty("删除标记，0未删除，1删除")
    @TableField("delete_Flag")
    private Integer deleteFlag;

    @ApiModelProperty("名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty("分类名称")
    @TableField(exist = false)
    private String categoryName;

    @ApiModelProperty("货号")
    @TableField(exist = false)
    private String accountNum;

    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String productParentCode;

    @ApiModelProperty("品牌名称")
    @TableField(exist = false)
    private String mfrsName;

    @ApiModelProperty("基础重量(kg)")
    @TableField(exist = false)
    private BigDecimal weight;

    @ApiModelProperty("基础体积(立方米)")
    @TableField(exist = false)
    private BigDecimal volume;

    @ApiModelProperty("单位")
    @TableField(exist = false)
    private ProductUnit productUnit;

    @ApiModelProperty("单位List")
    @TableField(exist = false)
    private JSONArray unitList;

    @ApiModelProperty("货位")
    @TableField(exist = false)
    private DepotStorage storage;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
