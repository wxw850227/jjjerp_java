package net.jjjerp.common.entity.supplier;

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
import java.util.List;

/**
 * 供应商/客户信息表
 *
 * @author jjjerp
 * @since 2024-04-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_supplier")
@ApiModel(value = "Supplier对象")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Long supplierId;

    @ApiModelProperty("供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商网址")
    private String website;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在辖区id")
    private Integer regionId;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("图片List")
    @TableField(exist = false)
    private List<String> imageList;

    @ApiModelProperty("供应商类型,0普通供应商,1直供供应商,2-1688供应商")
    private Integer supplierType;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("备注")
    private String description;

    @ApiModelProperty("是否启用,0停用,1启用")
    private Integer enabled;

    @ApiModelProperty("期初应付")
    @NotNull(message = "期初应付不能为空")
    private BigDecimal initial;

    @ApiModelProperty("已付预付款")
    private BigDecimal advancePay;

    @ApiModelProperty("当前应付")
    private BigDecimal allNeedPay;

    @ApiModelProperty("欠款")
    @NotNull(message = "欠款不能为空")
    private BigDecimal debtCredit;

    @ApiModelProperty("账期")
    private Long debtPeriod;

    @ApiModelProperty("传真")
    private String fax;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("纳税人识别号")
    private String taxNum;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("邮编")
    private String zip;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("操作员")
    private Long creator;

    @ApiModelProperty("删除标记，0未删除，1已删除")
    private Integer deleteFlag;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
