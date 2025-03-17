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
import java.io.Serializable;

/**
 * 仓库货位库存表
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_depot_storage_stock")
@ApiModel(value = "DepotStorageStock对象")
public class DepotStorageStock implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("货位库存id")
    @TableId(value = "storage_stock_id", type = IdType.AUTO)
    private Integer storageStockId;

    @ApiModelProperty("商品规格id")
    private Long productExtendId;

    @ApiModelProperty("库存数量")
    private Long stockNum;

    @ApiModelProperty("货位id")
    private Integer storageId;

    @ApiModelProperty("图片url")
    @TableField(exist = false)
    private String imageUrl;

    @ApiModelProperty("规格编码")
    @TableField(exist = false)
    private String productCode;

    @ApiModelProperty("多属性")
    @TableField(exist = false)
    private String sku;

    @ApiModelProperty("商品名称")
    @TableField(exist = false)
    private String productName;

    @ApiModelProperty("是否删除,1是,0否")
    @TableField(exist = false)
    private Integer isDelete;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
