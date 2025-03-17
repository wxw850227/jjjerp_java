package net.jjjerp.common.entity.product;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 多单位表
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_unit")
@ApiModel(value = "ProductUnit对象")
public class ProductUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "basic_unit_id", type = IdType.AUTO)
    @NotNull(message = "basicUnitId不能为空", groups = {Update.class})
    private Long basicUnitId;

    @ApiModelProperty("副单位List")
    @TableField(exist = false)
    private List<ProductDeputyUnit> deputyList;

    @ApiModelProperty("删除副单位IDList")
    @TableField(exist = false)
    private List<Integer> deleteList;

    @ApiModelProperty("名称，支持多单位")
    private String name;

    @ApiModelProperty("基础单位")
    @NotBlank(message = "基础单位不能为空")
    private String basicUnit;

    @ApiModelProperty("是否启用,0停用,1启用")
    private Integer enabled;

    @ApiModelProperty("应用id")
    private Long appId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
