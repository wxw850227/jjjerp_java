package net.jjjerp.common.vo.statement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "商品排行VO")
public class ProductBoardVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    private long productId;

    @ApiModelProperty("销售数量")
    private long marketNum;

    @ApiModelProperty("销售金额")
    private BigDecimal marketPrice;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品主图")
    private String imagePath;

    @ApiModelProperty("库存数量")
    private long stockNum;
}
