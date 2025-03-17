package net.jjjerp.admin.controller.refund.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"待处理售后单"})
@RestController
@RequestMapping("/admin/refund/flow/handle")
public class HandleController {

    @Resource
    private BillHeadService billHeadService;


    //已审核待处理售后单
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/handle/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.SHSQD.getValue());
        //审核状态，10待审核20通过30拒绝
        param.setAuditStatus(20);
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //入库
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/handle/put")
    @OperationLog(name = "put")
    @ApiOperation(value = "put", response = String.class)
    public ApiResult<String> put(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.XSTHD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //下单
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/handle/order")
    @OperationLog(name = "order")
    @ApiOperation(value = "order", response = String.class)
    public ApiResult<String> order(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.XSDD.getValue());
        if( StringUtils.isBlank(head.getLinkBillId()) || StringUtils.isBlank(head.getLinkBillNum())){
            throw new BusinessException("关联售后单号不能为空");
        }
        //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨,7销售单原路退回,8线下销售订单,9线上销售订单,10预售销售订单
        head.setBusinessType(BusinessTypeEnum.XXXSDD.getValue());
        head.setSystemRemark("【来自售后-换货】售后单号：" + head.getLinkBillNum());
        //是否换货单,0否,1是
        head.setIsExchange(1);
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //售后处理
    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    @RequiresPermissions("/refund/flow/handle/manage")
    @OperationLog(name = "manage")
    @ApiOperation(value = "manage", response = String.class)
    public ApiResult<String> manage(@RequestBody @Validated BillHead head) {
        if(billHeadService.refundManage(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

}
