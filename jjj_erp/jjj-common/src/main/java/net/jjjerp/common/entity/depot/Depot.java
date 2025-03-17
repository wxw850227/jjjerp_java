package net.jjjerp.common.entity.depot;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 仓库表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot")
@ApiModel(value = "Depot对象")
public class Depot implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "depot_id", type = IdType.AUTO)
    private Long depotId;

    @ApiModelProperty("仓库名称")
    @NotEmpty(message = "仓库名称不能为空")
    private String name;

    @ApiModelProperty("仓库编码")
    private String code;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("省ID")
    private Integer provinceId;

    @ApiModelProperty("市ID")
    private Integer cityId;

    @ApiModelProperty("区ID")
    private Integer regionId;

    @ApiModelProperty("仓库地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("邮编")
    private String zip;

    @ApiModelProperty("仓库类型,0正品仓,1次品仓,2门店仓")
    @NotNull(message = "仓库类型不能为空")
    private Integer type;

    @ApiModelProperty("仓库费用ID")
    private Integer costId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("启用")
    private Integer enabled;

    @ApiModelProperty("删除标记，0未删除，1删除")
    @TableField("delete_Flag")
    private Integer deleteFlag;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
