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
import java.util.List;

/**
 * 仓库库区表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot_reservoir")
@ApiModel(value = "DepotReservoir对象")
public class DepotReservoir implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库库区id")
    @TableId(value = "depot_reservoir_id", type = IdType.AUTO)
    private Integer depotReservoirId;

    @ApiModelProperty("库区名称")
    @NotBlank(message = "库区名称不能为空")
    private String reservoirName;

    @ApiModelProperty("库区编码")
    @NotBlank(message = "库区编码不能为空")
    private String reservoirCode;

    @ApiModelProperty("仓库id")
    @NotNull(message = "仓库id不能为空", groups = {Update.class})
    private Long  depotId;

    @ApiModelProperty("区域id")
    @NotNull(message = "区域id不能为空")
    private Integer areaId;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("货架List")
    @TableField(exist = false)
    private List<DepotShelf> shelfList;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
