package net.jjjerp.common.entity.bill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 用户收货地址表
 *
 * @author jjjerp
 * @since 2024-06-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_bill_address")
@ApiModel(value = "BillAddress对象")
public class BillAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "bill_address_id", type = IdType.AUTO)
    private Integer billAddressId;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("邮编")
    private String zip;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在区id")
    private Integer regionId;

    @ApiModelProperty("新市辖区(该字段用于记录region表中没有的市辖区)")
    private String district;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("单据id")
    private Long billId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
