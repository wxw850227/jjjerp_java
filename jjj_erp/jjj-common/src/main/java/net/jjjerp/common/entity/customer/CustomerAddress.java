package net.jjjerp.common.entity.customer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户收货地址表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@TableName("jjjerp_customer_address")
@ApiModel(value = "UserAddress对象")
public class CustomerAddress implements Serializable {
    private static final long serialVersionUID = 2L;

    @ApiModelProperty("主键id")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    @ApiModelProperty("收货人姓名")
    @NotBlank(message = "收货人不能为空")
    private String name;

    @ApiModelProperty("手机")
    @NotBlank(message = "手机不能为空")
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

    @ApiModelProperty("用户id")
    private Integer customerId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
