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
import net.jjjerp.common.entity.depot.Depot;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 单据主表
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_bill_head")
@ApiModel(value = "BillHead对象")
public class BillHead implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long billId;

    @ApiModelProperty("业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货")
    @TableField(exist = false)
    private String businessTypeString;

    @ApiModelProperty("订单状态文本内容")
    @TableField(exist = false)
    private String stateText;

    @ApiModelProperty("用户地址")
    @TableField(exist = false)
    private BillAddress billAddress;

    @ApiModelProperty("结算状态文本内容")
    @TableField(exist = false)
    private String settlementText;

    @ApiModelProperty("仓库名称")
    @TableField(exist = false)
    private String depotName;

    @ApiModelProperty("供应商名称")
    @TableField(exist = false)
    private String supplierName;

    @ApiModelProperty("业务员名称")
    @TableField(exist = false)
    private String salesName;

    @ApiModelProperty("审核员名称")
    @TableField(exist = false)
    private String auditName;

    @ApiModelProperty("操作员名称")
    @TableField(exist = false)
    private String creatorName;

    @ApiModelProperty("附件List")
    @TableField(exist = false)
    private List<String> fileList;

    @ApiModelProperty("单据商品List")
    @TableField(exist = false)
    private List<BillItem> billItemList;

    @ApiModelProperty("单据物流List")
    @TableField(exist = false)
    private List<BillDelivery> deliveryList;

    @ApiModelProperty("物流公司id")
    @TableField(exist = false)
    private Integer expressId;


    @ApiModelProperty("物流单号")
    @TableField(exist = false)
    private String expressNo;

    @ApiModelProperty("入库单")
    @TableField(exist = false)
    private List<BillHead> rKList;

    @ApiModelProperty("换货单")
    @TableField(exist = false)
    private List<BillHead> xDList;

    @ApiModelProperty("单据地址")
    @TableField(exist = false)
    private BillAddress address;

    @ApiModelProperty("提示")
    @TableField(exist = false)
    private String hint;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String storeName;

    @ApiModelProperty("买家名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty("结算账户名称")
    @TableField(exist = false)
    private String accountName;

    @ApiModelProperty("已收金额")
    @TableField(exist = false)
    private BigDecimal receivedMoney;

    @ApiModelProperty("单据应收款余额")
    @TableField(exist = false)
    private BigDecimal balanceMoney;


    @ApiModelProperty("单据类型,1采购订单,2采购入库单,3采购退货订单,4采购退货单,6销售订单,8销售出库单,10库存调拨单,11调拨出库单,12调拨入库单,13其他出库单,14其他入库单,15盘盈盘亏单,16期初库存单,17售后申请单,18销售退货单")
    private Integer billType;

    @ApiModelProperty("业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨,7销售单原路退回,8线下销售订单,9线上销售订单,10预售销售订单,11线下出库单")
    private Integer businessType;

    @ApiModelProperty("售后类型(0无售后 10退货退款 20换货 30仅退款 40补发 50 维修 60 其他)")
    private Integer refundType;

    @ApiModelProperty("是否换货单,0否,1是")
    private Integer isExchange;

    @ApiModelProperty("单据类型名称")
    private String typeName;

    @ApiModelProperty("单据编号")
    private String codeNum;

    @ApiModelProperty("出入库时间")
    private Date operTime;

    @ApiModelProperty("业务时间")
    private Date businessTime;

    @ApiModelProperty("业务时间")
    @TableField(exist = false)
    private String businessTimeStr;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("仓库id")
    private Long depotId;

    @ApiModelProperty("业务员id")
    private Long salesId;

    @ApiModelProperty("操作员id")
    private Integer creatorId;

    @ApiModelProperty("经手人id")
    private Integer handleId;

    @ApiModelProperty("审核员id")
    private Integer auditId;

    @ApiModelProperty("账户id")
    private Long accountId;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("配送方式(10快递发货 20上门自提 30无需物流 40多包裹)")
    private Integer deliveryType;

    @ApiModelProperty("合计金额")
    private BigDecimal totalPrice;

    @ApiModelProperty("商品总量")
    private Long totalNum;

    @ApiModelProperty("买家留言")
    private String buyerRemark;

    @ApiModelProperty("线上备注")
    private String onlineRemark;

    @ApiModelProperty("系统备注")
    private String systemRemark;

    @ApiModelProperty("处理详情")
    private String manageDetails;

    @ApiModelProperty("打印备注")
    private String printRemark;

    @ApiModelProperty("附件内容")
    private String fileUrls;

    @ApiModelProperty("业务原因")
    private String businessCause;

    @ApiModelProperty("问题原因")
    private String problemCause;

    @ApiModelProperty("问题描述")
    private String problemDescribe;

    @ApiModelProperty("优惠类型,1固定金额")
    private Integer discountType;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountMoney;

    @ApiModelProperty("欠款金额")
    private BigDecimal deptMoney;

    @ApiModelProperty("其他费用合计")
    private BigDecimal otherMoney;

    @ApiModelProperty("其他费用分摊类型,0不分摊,1按金额分摊,2按重量分摊")
    private Integer otherAllotType;

    @ApiModelProperty("采购运费分摊类型,0不分摊,1按金额分摊,2按重量分摊")
    private Integer purchaseAllotType;

    @ApiModelProperty("运费合计")
    private BigDecimal purchaseMoney;

    @ApiModelProperty("变动金额(收款,退款)")
    private BigDecimal deposit;

    @ApiModelProperty("赔付金额")
    private BigDecimal compensateMoney;

    @ApiModelProperty("客户收款账户")
    private String payee;

    @ApiModelProperty("审核状态，10待审核20通过30拒绝")
    private Integer auditStatus;

    @ApiModelProperty("单据状态，0未开始、1完成、2进行中(已配货),3已关闭,4异常,5待配货,6已发货")
    private Integer purchaseStatus;

    @ApiModelProperty("结算状态，0待结算、1已结算、2已结清")
    private Integer settlementStatus;

    @ApiModelProperty("关联单据id")
    private String linkBillId;

    @ApiModelProperty("关联单据编号")
    private String linkBillNum;

    @ApiModelProperty("结算方式(0现金,1预付款)")
    private String payType;

    @ApiModelProperty("关闭原因")
    private String closeReason;

    @ApiModelProperty("审核拒绝原因")
    private String refuseReason;

    @ApiModelProperty("异常原因")
    private String anomalousReason;

    @ApiModelProperty("对方仓库Id")
    private Long anotherDepotId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("租户id")
    private Long appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
