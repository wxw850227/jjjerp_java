package net.jjjerp.common.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 店铺记录表
 *
 * @author jjjerp
 * @since 2024-04-19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_store")
@ApiModel(value = "Store对象")
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺")
    @TableId(value = "store_id", type = IdType.AUTO)
    private Long storeId;

    @ApiModelProperty("店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String storeName;

    @ApiModelProperty("店铺昵称")
    @NotBlank(message = "店铺昵称不能为空")
    private String nickName;

    @ApiModelProperty("店铺网址")
    private String website;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("市ID")
    private Long cityId;

    @ApiModelProperty("区ID")
    private Long regionId;

    @ApiModelProperty("店铺类型")
    private Integer storeType;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("备注,宣传标语")
    private String description;

    @ApiModelProperty("key")
    private String appKey;

    @ApiModelProperty("secret")
    private String secret;

    @ApiModelProperty("是否启用,0停用,1启用")
    private Integer enabled;

    @ApiModelProperty("邮编")
    private String zip;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("操作员")
    private Long creator;

    @ApiModelProperty("绑定账户ID")
    @NotNull(message = "绑定账户不能为空")
    private Long accountId;

    @ApiModelProperty("总欠款")
    private BigDecimal debtCredit;

    @ApiModelProperty("删除标记，0未删除，1已删除")
    private Integer deleteFlag;

    @ApiModelProperty("授权有效期")
    private Date expiryTime;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
