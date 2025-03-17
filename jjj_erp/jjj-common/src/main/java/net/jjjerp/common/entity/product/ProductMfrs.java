package net.jjjerp.common.entity.product;

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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 产品品牌表
 *
 * @author jjjerp
 * @since 2024-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_mfrs")
@ApiModel(value = "ProductMfrs对象")
public class ProductMfrs implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("品牌id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("品牌名称")
    @NotBlank(message = "品牌名称不能为空")
    private String name;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
