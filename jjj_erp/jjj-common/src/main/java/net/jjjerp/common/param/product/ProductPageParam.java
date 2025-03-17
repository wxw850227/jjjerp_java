

package net.jjjerp.common.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.pagination.BasePageOrderParam;

import javax.validation.constraints.NotNull;

/**
 * 商品 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductParam对象", description = "商品查询参数")
public class ProductPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("启用 0-禁用  1-启用")
    private Integer enabled;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("产品类型id")
    private Integer categoryId;

    @ApiModelProperty("规格条码")
    private String extendBarCode;

    @ApiModelProperty("规格编码")
    private String productExtendCode;

    @ApiModelProperty("多属性")
    private String sku;

    @ApiModelProperty("商品货号")
    private String accountNum;

    @ApiModelProperty("商品条码")
    private String barCode;

    @ApiModelProperty("商品编码")
    private String productCode;

}
