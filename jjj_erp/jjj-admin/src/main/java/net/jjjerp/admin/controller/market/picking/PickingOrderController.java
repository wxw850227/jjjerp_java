package net.jjjerp.admin.controller.market.picking;

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
@Api(value = "index", tags = {"仓内拣货"})
@RestController
@RequestMapping("/admin/market/picking/order")
public class PickingOrderController {

    @Resource
    private BillHeadService billHeadService;

    //已审核待配货订单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/market/picking/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(20);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        param.setPurchaseStatus(5);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //确认配货
    @RequestMapping(value = "/picking", method = RequestMethod.POST)
    @RequiresPermissions("/market/picking/order/picking")
    @OperationLog(name = "picking")
    @ApiOperation(value = "picking", response = String.class)
    public ApiResult<String> picking(Long billId) {
        if(billHeadService.picking(billId)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

}
