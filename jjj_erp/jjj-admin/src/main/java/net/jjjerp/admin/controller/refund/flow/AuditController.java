package net.jjjerp.admin.controller.refund.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.common.vo.product.ProductStockVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"售后单审核"})
@RestController
@RequestMapping("/admin/refund/flow/audit")
public class AuditController {

    @Resource
    private BillHeadService billHeadService;

    //待审核售后单
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/audit/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.SHSQD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(10);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //审核售后单
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/audit/audit")
    @OperationLog(name = "audit")
    @ApiOperation(value = "audit", response = String.class)
    public ApiResult<String> audit(@RequestBody BillHead head) {
        if(billHeadService.auditRefundOrder(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //打回审核
    @RequestMapping(value = "/repulseAudit", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/audit/repulseAudit")
    @OperationLog(name = "repulseAudit")
    @ApiOperation(value = "repulseAudit", response = String.class)
    public ApiResult<String> repulseAudit(Long billId) {
        if(billHeadService.repulseAudit(billId)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //关闭
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/audit/close")
    @OperationLog(name = "close")
    @ApiOperation(value = "close", response = String.class)
    public ApiResult<String> close(Long billId, String closeReason) {
        if(billHeadService.closeOrder(billId, closeReason)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }
}
