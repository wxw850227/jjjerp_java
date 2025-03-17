package net.jjjerp.admin.controller.refund.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillDelivery;
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
@Api(value = "index", tags = {"新建售后单"})
@RestController
@RequestMapping("/admin/refund/flow/refund")
public class refundController {

    @Resource
    private BillHeadService billHeadService;

    //可退货的销售单列表
    @RequestMapping(value = "/marketIndex", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/refund/marketIndex")
    @OperationLog(name = "marketIndex")
    @ApiOperation(value = "marketIndex", response = String.class)
    public ApiResult<Map<String, Object>> marketIndex(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        return ApiResult.ok(billHeadService.bill(param));
    }

    //新建售后单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/refund/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.SHSQD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/refund/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated BillHead head) {
        if(billHeadService.editOrder(head)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //修改快递
    @RequestMapping(value = "/editDeliver", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/refund/editDeliver")
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
