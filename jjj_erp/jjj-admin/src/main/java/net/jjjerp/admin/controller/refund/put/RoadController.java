package net.jjjerp.admin.controller.refund.put;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"原路退回"})
@RestController
@RequestMapping("/admin/refund/put/road")
public class RoadController {

    @Resource
    private BillHeadService billHeadService;


    //已发货销售单(未收货,未售后)
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/refund/put/road/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> scanIndex(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.XSDD.getValue());
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        param.setPurchaseStatus(6);
        return ApiResult.ok(billHeadService.scanIndex(param));
    }


}
