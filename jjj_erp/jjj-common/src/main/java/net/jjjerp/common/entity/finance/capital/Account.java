package net.jjjerp.common.entity.finance.capital;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 收支账户表
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_account")
@ApiModel(value = "Account对象")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("账户ID")
    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

    @ApiModelProperty("本期收款")
    @TableField(exist = false)
    private BigDecimal receipt;

    @ApiModelProperty("本期付款")
    @TableField(exist = false)
    private BigDecimal payment;

    @ApiModelProperty("本期变化金额")
    @TableField(exist = false)
    private BigDecimal change;

    @ApiModelProperty("期末余额")
    @TableField(exist = false)
    private BigDecimal balance;

    @ApiModelProperty("账户类型,1现金,2银行,3第三方")
    @NotNull(message = "账户类型不能为空")
    private Integer accountType;

    @ApiModelProperty("账户账号")
    private String accountNumber;

    @ApiModelProperty("账户名称")
    @NotBlank(message = "账户名称不能为空")
    private String accountName;

    @ApiModelProperty("机构名称")
    private String organName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("期初余额")
    @NotNull(message = "期初余额不能为空")
    private BigDecimal openingMoney;

    @ApiModelProperty("当前余额")
    private BigDecimal nowMoney;

    @ApiModelProperty("是否默认，0不是，1是")
    @TableField(exist = false)
    private Integer isDefault;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty("启用 0-禁用  1-启用")
    private Integer enabled;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
