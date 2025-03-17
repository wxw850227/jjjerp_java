package net.jjjerp.admin.controller.statement.market;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.finance.capital.AccountMoneyLogService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"报表"})
@RestController
@RequestMapping("/admin/statement/market/board")
public class BoardController {

    @Autowired
    private BillHeadService billHeadService;

    //作业看板
    @RequestMapping(value = "/job", method = RequestMethod.POST)
    @RequiresPermissions("/statement/market/board/job")
    @OperationLog(name = "作业看板")
    @ApiOperation(value = "job", response = String.class)
    public ApiResult<Map<String, Object>> jobBoard(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.jobBoard(param));
    }

    //交易趋势
    @RequestMapping(value = "/deal", method = RequestMethod.POST)
    @RequiresPermissions("/statement/market/board/deal")
    @OperationLog(name = "交易趋势")
    @ApiOperation(value = "deal", response = String.class)
    public ApiResult<Map<String, Object>> dealBoard(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.dealBoard(param));
    }

    //库存看板
    @RequestMapping(value = "/stock", method = RequestMethod.POST)
    @RequiresPermissions("/statement/market/board/stock")
    @OperationLog(name = "库存看板")
    @ApiOperation(value = "stock", response = String.class)
    public ApiResult<Map<String, Object>> stockBoard(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.stockBoard(param));
    }

    //商品排行
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @RequiresPermissions("/statement/market/board/product")
    @OperationLog(name = "商品排行")
    @ApiOperation(value = "product", response = String.class)
    public ApiResult<Map<String, Object>> productBoard(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.productBoard(param));
    }

}
