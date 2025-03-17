package net.jjjerp.common.entity.finance.capital;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.bill.BillHead;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 账户资金变动明细表
 *
 * @author jjjerp
 * @since 2024-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_account_money_log")
@ApiModel(value = "AccountMoneyLog对象")
public class AccountMoneyLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String storeName;

    @ApiModelProperty("单据类型名称")
    @TableField(exist = false)
    private String typeName;

    @ApiModelProperty("收支方向,1支出,2收入")
    @TableField(exist = false)
    private String directionTypeName;

    @ApiModelProperty("交易往来单位")
    @TableField(exist = false)
    private String tradingUnitName;

    @ApiModelProperty("账户名称")
    @TableField(exist = false)
    private String accountName;

    @ApiModelProperty("账户id")
    private Long accountId;

    @ApiModelProperty("单据id")
    private Long billId;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("单据类型")
    private Integer billType;

    @ApiModelProperty("收支方向,1支出,2收入")
    private Integer directionType;

    @ApiModelProperty("交易往来单位,0其他,1客户,2供应商,3快递")
    private Integer tradingUnit;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("供应商ID")
    private Long supplierId;

    @ApiModelProperty("变动金额")
    private BigDecimal money;

    @ApiModelProperty("账户余额")
    private BigDecimal balanceMoney;

    @ApiModelProperty("摘要/说明")
    private String description;

    @ApiModelProperty("凭证附件内容")
    private String fileUrls;

    @ApiModelProperty("业务时间")
    private Date billTime;

    @ApiModelProperty("小程序商城id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
