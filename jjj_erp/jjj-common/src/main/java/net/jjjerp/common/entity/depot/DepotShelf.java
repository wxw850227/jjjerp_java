package net.jjjerp.common.entity.depot;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.validator.groups.Add;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 仓库货架表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot_shelf")
@ApiModel(value = "DepotShelf对象")
public class DepotShelf implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("货架id")
    @TableId(value = "depot_shelf_id", type = IdType.AUTO)
    private Integer depotShelfId;

    @ApiModelProperty("库区名称")
    @TableField(exist = false)
    private String reservoirName;

    @ApiModelProperty("货架名称")
    @NotBlank(message = "货架名称不能为空")
    private String shelfName;

    @ApiModelProperty("货架编码")
    @NotBlank(message = "货架编码不能为空")
    private String shelfCode;

    @ApiModelProperty("仓库id")
    @NotNull(message = "仓库id不能为空", groups = {Update.class})
    private Long depotId;

    @ApiModelProperty("库区id")
    @NotNull(message = "库区id不能为空")
    private Integer reservoirId;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
