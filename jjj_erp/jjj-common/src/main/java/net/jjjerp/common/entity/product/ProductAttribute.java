package net.jjjerp.common.entity.product;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * 产品属性表
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_attribute")
@ApiModel(value = "ProductAttribute对象")
public class ProductAttribute implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("属性名")
    private String attributeName;

    @ApiModelProperty("属性值")
    private String attributeValue;

    @ApiModelProperty("属性值List")
    @TableField(exist = false)
    private List<String> valueList;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
