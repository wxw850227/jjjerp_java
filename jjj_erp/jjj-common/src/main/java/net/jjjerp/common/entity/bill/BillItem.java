package net.jjjerp.common.entity.bill;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.product.ProductExtend;

import java.io.Serializable;

/**
 * 单据子表
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_bill_item")
@ApiModel(value = "BillItem对象")
public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "bill_item_id", type = IdType.AUTO)
    private Long billItemId;

    @ApiModelProperty("表头Id")
    private Long billId;

    @ApiModelProperty("商品Id")
    private Long productId;

    @ApiModelProperty("商品扩展id")
    private Long productExtendId;

    @ApiModelProperty("商品扩展")
    @TableField(exist = false)
    private ProductExtend productExtend;

    @ApiModelProperty("单据类型名称")
    @TableField(exist = false)
    private String typeName;

    @ApiModelProperty("单据编号")
    @TableField(exist = false)
    private String codeNum;

    @ApiModelProperty("主单据")
    @TableField(exist = false)
    private BillHead head;

    @ApiModelProperty("提示")
    @TableField(exist = false)
    private String hint;

    @ApiModelProperty("商品计量单位")
    private String materialUnit;

    @ApiModelProperty("多属性")
    private String sku;

    @ApiModelProperty("实际操作数量")
    private Long operNumber;

    @ApiModelProperty("实际操作总数量")
    private Long basicNumber;

    @ApiModelProperty("待入库数量")
    private Long noArrivalNumber;

    @ApiModelProperty("待入库总数量")
    private Long noArrivalAllNumber;

    @ApiModelProperty("可操作数量(可入库,可出库,可退货)")
    private Long availableNumber;

    @ApiModelProperty("可操作总数量(可入库,可出库,可退货)")
    private Long availableAllNumber;

    @ApiModelProperty("已退数量)")
    private Long returnNumber;

    @ApiModelProperty("已退总数")
    private Long returnAllNumber;

    @ApiModelProperty("调出数量")
    private Long calloutNumber;

    @ApiModelProperty("调出总数")
    private Long calloutAllNumber;

    @ApiModelProperty("调入数量)")
    private Long foldNumber;

    @ApiModelProperty("调入总数")
    private Long foldAllNumber;

    @ApiModelProperty("商品库存余量")
    private Long stockNum;

    @ApiModelProperty("锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量")
    private Long freezeStock;

    @ApiModelProperty("库存变化数量")
    private Long differenceNum;

    @ApiModelProperty("关联的订单子单号")
    private Long linkItemId;

    @ApiModelProperty("单据类型,1采购订单,2采购入库单,3采购退货订单,4采购退货单,6销售订单,8销售出库单,10库存调拨单,11调拨出库单,12调拨入库单,13其他出库单,14其他入库单,15盘盈盘亏单,16期初库存单,17售后申请单,18销售退货单")
    private Integer billType;

    @ApiModelProperty("商品绑定货位")
    @TableField(exist = false)
    private DepotStorage storage;

    @ApiModelProperty("规格条码")
    @TableField(exist = false)
    private String barCode;

    @ApiModelProperty("业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨")
    private Integer businessType;

    @ApiModelProperty("是否换货商品,0否,1是")
    private Integer isExchange;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("吊牌价")
    private BigDecimal commodityDecimal;

    @ApiModelProperty("折扣率")
    private BigDecimal discountRate;

    @ApiModelProperty("折后单价")
    private BigDecimal discountPrice;

    @ApiModelProperty("总价")
    private BigDecimal allPrice;

    @ApiModelProperty("分摊金额")
    private BigDecimal allotPrice;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("仓库ID")
    private Long depotId;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("仓库货位ID")
    private Long depotStorageId;

    @ApiModelProperty("采购周期")
    private Long purchaseCycle;

    @ApiModelProperty("调拨时，对方仓库Id")
    private Long anotherDepotId;

    @ApiModelProperty("预计到货日期")
    private Date arrivalDate;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer isDelete;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
