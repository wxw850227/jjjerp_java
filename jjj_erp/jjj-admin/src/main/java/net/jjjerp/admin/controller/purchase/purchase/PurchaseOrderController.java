package net.jjjerp.admin.controller.purchase.purchase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.enums.BillTypeEnum;
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
@Api(value = "index", tags = {"采购订单"})
@RestController
@RequestMapping("/admin/purchase/purchase/order")
public class PurchaseOrderController {

    @Resource
    private BillHeadService billHeadService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.CGDD.getValue());
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.CGDD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated BillHead head) {
        if(billHeadService.editOrder(head)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //关闭
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/close")
    @OperationLog(name = "close")
    @ApiOperation(value = "close", response = String.class)
    public ApiResult<String> close(Long billId, String closeReason) {
        if(billHeadService.closeOrder(billId, closeReason)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //审核
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/audit")
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@RequestBody BillHead head) {
        if(billHeadService.auditOrder(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //设置物流
    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    @RequiresPermissions("/purchase/purchase/order/delivery")
    @OperationLog(name = "delivery")
    @ApiOperation(value = "delivery", response = String.class)
    public ApiResult<String> delivery(@RequestBody BillHead head) {
        if(billHeadService.addOrderDelivery(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }


}
