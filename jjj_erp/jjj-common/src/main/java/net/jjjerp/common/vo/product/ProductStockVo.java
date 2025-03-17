package net.jjjerp.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品规格库存VO")
public class ProductStockVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库ID")
    private Long depotId;

    @ApiModelProperty("仓库名称")
    private String depotName;

    @ApiModelProperty("实际总库存,初始默认为0")
    private Long totalStock;

    @ApiModelProperty("锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量")
    private Long freezeStock;

    @ApiModelProperty("当前可用库存(实际库存-锁定库存)")
    private Long stockNum;
}
