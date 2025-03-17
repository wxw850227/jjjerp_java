package net.jjjerp.common.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.pagination.BasePageOrderParam;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公共分页查询参数", description = "没有参数的分页")
public class CommonPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索条件")
    private String search;

    @ApiModelProperty("属性名")
    private String attributeName;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("客户昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("是否启用,0停用,1启用")
    private Integer enabled;

    @ApiModelProperty("是否启用,0停用,1启用")
    private Integer state;

    @ApiModelProperty("调拨类型,0待出库,1待入库")
    private Integer allot;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("仓库id")
    private Long depotId;

    @ApiModelProperty("区域id")
    private Integer areaId;

    @ApiModelProperty("库区id")
    private Integer reservoirId;

    @ApiModelProperty("货架id")
    private Integer depotShelfId;

    @ApiModelProperty("商品规格id")
    private Long productExtendId;

    @ApiModelProperty("单据类型")
    private Integer billType;

    @ApiModelProperty("单据类型字符串")
    private String billTypeStr;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("审核状态，10待审核20通过30拒绝")
    private Integer auditStatus;

    @ApiModelProperty("单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货")
    private Integer purchaseStatus;

    @ApiModelProperty("规格条码")
    private String extendBarCode;

    @ApiModelProperty("规格编码")
    private String productExtendCode;

    @ApiModelProperty("多属性")
    private String sku;

    @ApiModelProperty("货号")
    private String accountNum;

    @ApiModelProperty("商品条码")
    private String barCode;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("仓库ID数组")
    private List<Long> depotIdList;

    @ApiModelProperty("品牌")
    private Integer mfrs;

    @ApiModelProperty("product_id")
    private Long productId;

    @ApiModelProperty("查询日期")
    private List<String> time;

    @ApiModelProperty("客户id")
    private Integer customerId;

    @ApiModelProperty("账户ID")
    private Long accountId;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("查询日期类型,0记账时间,1业务日期")
    private int timeType;

    @ApiModelProperty("收支方向,1支出,2收入")
    private Integer directionType;

    @ApiModelProperty("交易往来单位,0其他,1客户,2供应商,3快递")
    private Integer tradingUnit;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("单据编号")
    private String codeNum;

    @ApiModelProperty("应用Id")
    private Integer appId;

    @ApiModelProperty("查询报表日期类型,0最近一周,1最近一个月,3本年")
    private int dateType;

    @ApiModelProperty("开始搜索时间")
    private String startDate;

    @ApiModelProperty("结束搜索时间")
    private String endDate;
}
