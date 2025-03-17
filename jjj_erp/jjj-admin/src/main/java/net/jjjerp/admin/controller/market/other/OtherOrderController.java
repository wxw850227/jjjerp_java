package net.jjjerp.admin.controller.market.other;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"销售-其他"})
@RestController
@RequestMapping("/admin/market/other/order")
public class OtherOrderController {

    @Resource
    private BillHeadService billHeadService;

    //订单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/market/other/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(20);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //异常订单列表
    @RequestMapping(value = "/abnormalIndex", method = RequestMethod.POST)
    @RequiresPermissions("/market/other/order/abnormalIndex")
    @OperationLog(name = "abnormalIndex")
    @ApiOperation(value = "abnormalIndex", response = String.class)
    public ApiResult<Map<String, Object>> abnormalIndex(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //单据状态，0未采购、1完成采购|销售单已发货、2部分采购|销售单待发货,3已关闭,4异常,5待配货
        param.setPurchaseStatus(4);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //关闭异常订单
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @RequiresPermissions("/market/other/order/close")
    @OperationLog(name = "close")
    @ApiOperation(value = "close", response = String.class)
    public ApiResult<String> close(Long billId, String closeReason) {
        if(billHeadService.closeMarketOrder(billId, closeReason)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //恢复异常订单
    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    @RequiresPermissions("/market/other/order/recover")
    @OperationLog(name = "recover")
    @ApiOperation(value = "recover", response = String.class)
    public ApiResult<String> recover(Long billId) {
        if(billHeadService.recoverMarketOrder(billId)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //已发货订单列表
    @RequestMapping(value = "/deliverIndex", method = RequestMethod.POST)
    @RequiresPermissions("/market/other/order/deliverIndex")
    @OperationLog(name = "deliverIndex")
    @ApiOperation(value = "deliverIndex", response = String.class)
    public ApiResult<Map<String, Object>> deliverIndex(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(20);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        param.setPurchaseStatus(6);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

}
