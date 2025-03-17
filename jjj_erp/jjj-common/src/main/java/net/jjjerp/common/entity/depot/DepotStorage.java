package net.jjjerp.common.entity.depot;

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
 * 仓库货位表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot_storage")
@ApiModel(value = "DepotStorage对象")
public class DepotStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库货位id")
    @TableId(value = "depot_storage_id", type = IdType.AUTO)
    private Integer depotStorageId;

    @ApiModelProperty("货位编码")
    @NotBlank(message = "货位编码不能为空")
    private String storageCode;

    @ApiModelProperty("货架id")
    private Integer sheifId;

    @ApiModelProperty("库区id")
    private Integer reservoirId;

    @ApiModelProperty("仓库id")
    private Long depotId;

    @ApiModelProperty("货位类型,1拣货货位,2存储货位,3异常货位")
    @NotNull(message = "货位类型不能为空")
    private Integer storageType;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("库存List")
    @TableField(exist = false)
    private List<DepotStorageStock> stockList;

    @ApiModelProperty("库存")
    @TableField(exist = false)
    private DepotStorageStock stock;

    @ApiModelProperty("库存字符串")
    @TableField(exist = false)
    private String stockStr;

    @ApiModelProperty("库区名称")
    @TableField(exist = false)
    private String reservoirName;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
