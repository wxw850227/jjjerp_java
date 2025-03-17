package net.jjjerp.admin.controller.market.deliver;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"发货出库"})
@RestController
@RequestMapping("/admin/market/deliver/order")
public class DeliverOrderController {

    @Resource
    private BillHeadService billHeadService;

    //已配货待发货订单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/market/deliver/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(20);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        param.setPurchaseStatus(2);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //出库单列表
    @RequestMapping(value = "/ckIndex", method = RequestMethod.POST)
    @RequiresPermissions("/market/deliver/order/ckIndex")
    @OperationLog(name = "ckIndex")
    @ApiOperation(value = "ckIndex", response = String.class)
    public ApiResult<Map<String, Object>> ckIndex(@RequestBody CommonPageParam param) {
        //出库单
        param.setBillType(BillTypeEnum.XSCKD.getValue());
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //确认发货
    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    @RequiresPermissions("/market/deliver/order/deliver")
    @OperationLog(name = "deliver")
    @ApiOperation(value = "deliver", response = String.class)
    public ApiResult<String> deliver(Long billId) {
        if(billHeadService.deliver(billId)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //新增线下出库单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/market/deliver/order/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> addCk(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.XSCKD.getValue());
        //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨,7销售单原路退回,8线下销售订单,9线上销售订单,10预售销售订单,11线下出库单
        head.setBusinessType(BusinessTypeEnum.XXCKD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

}
