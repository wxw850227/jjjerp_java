package net.jjjerp.admin.controller.stock.condition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.common.vo.product.ProductStockVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
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
@Api(value = "index", tags = {"库存状况"})
@RestController
@RequestMapping("/admin/stock/condition/condition")
public class ConditionController {

    @Resource
    private BillHeadService billHeadService;
    @Autowired
    private ProductExtendService productExtendService;

    //商品规格库存状况
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/stock/condition/condition/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        return ApiResult.ok(productExtendService.conditionIndex(param));
    }

    //商品规格库存变化详情
    @RequestMapping(value = "/stockIndex", method = RequestMethod.POST)
    @RequiresPermissions("/stock/condition/condition/stockIndex")
    @OperationLog(name = "stockIndex")
    @ApiOperation(value = "stockIndex", response = String.class)
    public ApiResult<Map<String, Object>> stockIndex(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.stockIndex(param));
    }

    //商品规格仓库库存
    @RequestMapping(value = "/depotIndex", method = RequestMethod.POST)
    @RequiresPermissions("/stock/condition/condition/depotIndex")
    @OperationLog(name = "depotIndex")
    @ApiOperation(value = "depotIndex", response = String.class)
    public ApiResult<List<ProductStockVo>> depotIndex(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.depotIndex(param));
    }

}
