

package net.jjjerp.common.param.depot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * StorageParam参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "StorageParam", description = "StorageParam")
public class StorageParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库id")
    @NotNull(message = "仓库id不能为空")
    private Long depotId;

    @ApiModelProperty("库区id")
    @NotNull(message = "库区id不能为空")
    private Integer reservoirId;

    @ApiModelProperty("货架id")
    private Integer sheifId;

    @ApiModelProperty("库区名称")
    @NotBlank(message = "库区名称不能为空")
    private String reservoirName;

    @ApiModelProperty("货位类型,1拣货货位,2存储货位,3异常货位")
    @NotNull(message = "货位类型不能为空")
    private Integer storageType;

    @ApiModelProperty("巷道名称")
    @NotNull(message = "巷道名称不能为空")
    private Integer row;

    @ApiModelProperty("巷道数量")
    @NotNull(message = "巷道数量不能为空")
    private Integer addRow;

    @ApiModelProperty("架名称")
    @NotNull(message = "架名称不能为空")
    private Integer columns;

    @ApiModelProperty("架数量")
    @NotNull(message = "架数量不能为空")
    private Integer addCol;

    @ApiModelProperty("层名称")
    @NotNull(message = "层名称不能为空")
    private Integer floor;

    @ApiModelProperty("层数量")
    @NotNull(message = "层数量不能为空")
    private Integer addFloor;

    @ApiModelProperty("格名称")
    @NotNull(message = "格名称不能为空")
    private Integer cell;

    @ApiModelProperty("格数量")
    @NotNull(message = "格数量不能为空")
    private Integer addCell;

    @ApiModelProperty("类型,0预览,1添加")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
