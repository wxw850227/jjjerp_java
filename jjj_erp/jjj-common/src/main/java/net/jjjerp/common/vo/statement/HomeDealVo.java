package net.jjjerp.common.vo.statement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "首页交易趋势VO")
public class HomeDealVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付笔数")
    private BigDecimal depositNum;

    @ApiModelProperty("支付金额")
    private BigDecimal depositMoney;

    @ApiModelProperty("查询日期")
    private String day;
}
