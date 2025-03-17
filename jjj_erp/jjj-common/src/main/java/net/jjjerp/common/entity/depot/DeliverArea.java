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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 配送区域表
 *
 * @author jjjerp
 * @since 2024-04-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_deliver_area")
@ApiModel(value = "DeliverArea对象")
public class DeliverArea implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配送区域id")
    @TableId(value = "deliver_area_id", type = IdType.AUTO)
    private Integer deliverAreaId;

    @ApiModelProperty("区域名称")
    @NotBlank(message = "区域名称不能为空")
    private String name;

    @ApiModelProperty("省id字符串")
    private String provinceIds;

    @ApiModelProperty("省名称字符串")
    private String provinceNames;

    @ApiModelProperty("省id List")
    @TableField(exist = false)
    @NotEmpty(message = "省id List不能为空")
    private List<Integer> provinceIdList;

    @ApiModelProperty("省名称 List")
    @TableField(exist = false)
    @NotEmpty(message = "省名称 List不能为空")
    private List<String> provinceNameList;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Integer status;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
