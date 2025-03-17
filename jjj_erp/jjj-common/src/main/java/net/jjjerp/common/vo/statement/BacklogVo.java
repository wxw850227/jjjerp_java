package net.jjjerp.common.vo.statement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "待办VO")
public class BacklogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("待审核")
    private int daiShenHe;

    @ApiModelProperty("待发货")
    private int daiFaHuo;

    @ApiModelProperty("待配货")
    private int daiPeiHuo;

    @ApiModelProperty("待处理售后")
    private int daiShouHou;

    @ApiModelProperty("异常订单")
    private int yiChang;

    @ApiModelProperty("补发")
    private int buFa;

    @ApiModelProperty("其他")
    private int qiTa;

    @ApiModelProperty("退货退款")
    private int tuiHuo;

    @ApiModelProperty("仅退款")
    private int tuiKuan;

    @ApiModelProperty("换货")
    private int huanHuo;
}
