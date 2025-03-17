package net.jjjerp.admin.controller.stock.allot;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"调拨出库单"})
@RestController
@RequestMapping("/admin/stock/allot/allocateOut")
public class AllocateOutController {

    @Resource
    private BillHeadService billHeadService;

    //调拨出库单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/allocateOut/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.DBCKD.getValue());
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //待出库调拨单
    @RequestMapping(value = "/indexOut", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/allocateOut/indexOut")
    @OperationLog(name = "indexOut")
    @ApiOperation(value = "indexOut", response = String.class)
    public ApiResult<Map<String, Object>> indexOut(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.KCDBD.getValue());
        //调拨类型,0出库,1入库
        param.setAllot(0);
        return ApiResult.ok(billHeadService.bill(param));
    }

    //新增调拨出库单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/allocateOut/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.DBCKD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }
}
