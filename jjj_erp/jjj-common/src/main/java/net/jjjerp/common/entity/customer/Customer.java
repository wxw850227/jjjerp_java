package net.jjjerp.common.entity.customer;

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
import java.util.List;

/**
 * 客户记录表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_customer")
@ApiModel(value = "customer对象")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("客户id")
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Integer customerId;

    @ApiModelProperty("平台类型,1其他,2线下门店")
    @NotNull(message = "平台类型不能为空")
    private Integer platformType;

    @ApiModelProperty("客户昵称")
    @NotBlank(message = "客户昵称不能为空")
    private String nickName;

    @ApiModelProperty("客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String realName;

    @ApiModelProperty("客户编码")
    private String userCode;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("期初应收款")
    private BigDecimal initial;

    @ApiModelProperty("预付金额总额")
    private BigDecimal deposit;

    @ApiModelProperty("应收款总额")
    private BigDecimal allNeedPay;

    @ApiModelProperty("欠款总额")
    private BigDecimal debtCredit;

    @ApiModelProperty("账期")
    private Long debtPeriod;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("市ID")
    private Long cityId;

    @ApiModelProperty("区ID")
    private Long regionId;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("客户类型,0零售.1批发,2分销")
    private Integer userType;

    @ApiModelProperty("指定价格类型,0标准售价.1批发价,2吊牌价 ")
    private Integer priceType;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("备注")
    private String description;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("开票抬头")
    private String billHead;

    @ApiModelProperty("税号")
    private String taxNum;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String accountNumber;

    @ApiModelProperty("开票电话")
    private String billPhone;

    @ApiModelProperty("开票地址")
    private String billAddress;

    @ApiModelProperty("性别0=女1=男2=未知")
    private Integer gender;

    @ApiModelProperty("地址List")
    @TableField(exist = false)
    private List<CustomerAddress> addressList;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
