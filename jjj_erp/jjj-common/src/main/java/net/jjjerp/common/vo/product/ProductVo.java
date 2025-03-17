package net.jjjerp.common.vo.product;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.product.ProductUnit;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品VO")
public class ProductVo extends Product {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

    @ApiModelProperty("商品分类名称")
    private String categoryName;

    @ApiModelProperty("品牌名称")
    private String mfrsName;

    @ApiModelProperty("单位名称")
    private String unitName;

    //编辑使用
    @ApiModelProperty("商品sku")
    private List<ProductExtend> skuList;

    //编辑使用
    @ApiModelProperty("商品图片集合")
    private List<ProductImageVo> image;

    @ApiModelProperty("实际总库存,初始默认为0")
    private Long totalStock;

    @ApiModelProperty("锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量")
    private Long freezeStock;

    @ApiModelProperty("当前可用库存(实际库存-锁定库存)")
    private Long stockNum;

    @ApiModelProperty("期初库存")
    private Long openningStock;

    @ApiModelProperty("成本价,初始默认为0")
    private BigDecimal costPrice;
}
