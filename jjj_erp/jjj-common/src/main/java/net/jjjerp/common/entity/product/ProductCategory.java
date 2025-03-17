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
import java.io.Serializable;

/**
 * 产品类型表
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_category")
@ApiModel(value = "ProductCategory对象")
public class ProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品分类id")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("上级分类id")
    private Integer parentId;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
