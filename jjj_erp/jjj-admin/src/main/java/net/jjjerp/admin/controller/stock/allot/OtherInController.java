package net.jjjerp.admin.controller.stock.allot;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillDelivery;
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
@Api(value = "index", tags = {"其他入库单"})
@RestController
@RequestMapping("/admin/stock/allot/otherIn")
public class OtherInController {

    @Resource
    private BillHeadService billHeadService;

    //其他入库单列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/otherIn/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "其他入库单列表", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        param.setBillType(BillTypeEnum.QTRKD.getValue());
        return ApiResult.ok(billHeadService.indexOrder(param));
    }

    //新增其他入库单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/otherIn/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "新增其他入库单", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated BillHead head) {
        head.setBillType(BillTypeEnum.QTRKD.getValue());
        if(billHeadService.addOrder(head)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    //修改快递
    @RequestMapping(value = "/editDeliver", method = RequestMethod.POST)
    @RequiresPermissions("/stock/allot/otherIn/editDeliver")
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
