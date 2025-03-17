package net.jjjerp.admin.controller.market.audit;

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
@Api(value = "index", tags = {"销售审单"})
@RestController
@RequestMapping("/admin/market/audit/order")
public class MarketOrderController {

    @Resource
    private BillHeadService billHeadService;

    //待审核订单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(10);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.XSDD.getValue());
        //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨,7销售单原路退回,8线下销售订单,9线上销售订单,10预售销售订单
        head.setBusinessType(BusinessTypeEnum.XXXSDD.getValue());
        //单据状态，0未开始、1完成、2进行中(已配货),3已关闭,4异常,5待配货,6已发货
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated BillHead head) {
        if(billHeadService.editOrder(head)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //审核销售订单
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/audit")
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@RequestBody BillHead head) {
        if(billHeadService.auditMarketOrder(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //打回审核
    @RequestMapping(value = "/repulseAudit", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/repulseAudit")
    @OperationLog(name = "repulseAudit")
    @ApiOperation(value = "repulseAudit", response = String.class)
    public ApiResult<String> repulseAudit(Long billId) {
        if(billHeadService.repulseAudit(billId)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //提交异常订单
    @RequestMapping(value = "/abnormal", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/abnormal")
    @OperationLog(name = "abnormal")
    @ApiOperation(value = "abnormal", response = String.class)
    public ApiResult<String> abnormal(@RequestBody BillHead head) {
        if(billHeadService.abnormal(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //修改快递
    @RequestMapping(value = "/editDeliver", method = RequestMethod.POST)
    @RequiresPermissions("/market/audit/order/editDeliver")
    @OperationLog(name = "editDeliver")
    @ApiOperation(value = "editDeliver", response = String.class)
    public ApiResult<String> editDeliver(@RequestBody @Validated BillDelivery delivery) {
        if(billHeadService.editDeliver(delivery)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }



}
