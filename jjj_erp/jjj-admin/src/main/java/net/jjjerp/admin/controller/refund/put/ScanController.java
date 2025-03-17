package net.jjjerp.admin.controller.refund.put;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillItem;
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
@Api(value = "index", tags = {"扫描入库"})
@RestController
@RequestMapping("/admin/refund/put/scan")
public class ScanController {

    @Resource
    private BillHeadService billHeadService;


    //已审核售后单
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/refund/put/scan/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> scanIndex(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.SHSQD.getValue());
        return ApiResult.ok(billHeadService.scanIndex(param));
    }

}
