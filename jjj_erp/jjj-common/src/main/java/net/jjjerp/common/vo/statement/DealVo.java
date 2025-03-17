package net.jjjerp.common.vo.statement;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "交易趋势VO")
public class DealVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付笔数")
    private int depositNum;

    @ApiModelProperty("支付金额")
    private BigDecimal depositMoney;

    @ApiModelProperty("支付客户数")
    private int customerNum;

    @ApiModelProperty("平均客单价")
    private BigDecimal customerMoney;

    @ApiModelProperty("发货笔数")
    private int deliverNum;

    @ApiModelProperty("发货金额")
    private BigDecimal deliverMoney;

    @ApiModelProperty("查询日期")
    private String day;
}
