package net.jjjerp.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "LabelCancelParam对象", description = "电子面单取消参数")
public class LabelCanceParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderId;

    @ApiModelProperty("物流公司Id")
    private Integer expressId;

    @ApiModelProperty("电子面单设置Id")
    private Integer labelSettingId;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("电子面单订单号")
    private String kdOrderNum;

    @ApiModelProperty("appId")
    private Long appId;
}
