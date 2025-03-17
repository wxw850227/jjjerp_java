package net.jjjerp.common.entity.bill;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 单据物流表
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_bill_delivery")
@ApiModel(value = "BillDelivery对象")
public class BillDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "bill_delivery_id", type = IdType.AUTO)
    private Long billDeliveryId;

    @ApiModelProperty("单据ID")
    @NotNull(message = "单据ID不能为空")
    private Long billId;

    @ApiModelProperty("快递公司id")
    private Integer expressId;

    @ApiModelProperty("快递公司名称")
    private String expressName;

    @ApiModelProperty("快递单号")
    private String expressNo;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer isDelete;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
