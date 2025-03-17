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
 * 仓库区域表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot_area")
@ApiModel(value = "DepotArea对象")
public class DepotArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库区域id")
    @TableId(value = "depot_area_id", type = IdType.AUTO)
    private Integer depotAreaId;

    @ApiModelProperty("区域名称")
    @NotBlank(message = "区域名称不能为空")
    private String areaName;

    @ApiModelProperty("区域编码")
    @NotBlank(message = "区域编码不能为空")
    private String areaCode;

    @ApiModelProperty("仓库id")
    @NotNull(message = "仓库id不能为空")
    private Long depotId;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("库区List")
    @TableField(exist = false)
    private List<DepotReservoir> reservoirList;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
