package net.jjjerp.admin.controller.finance.capital;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.finance.capital.AccountMoneyLog;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.finance.capital.AccountMoneyLogService;
import net.jjjerp.admin.service.finance.capital.AccountService;
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
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"资金流水"})
@RestController
@RequestMapping("/admin/finance/capital/log")
public class LogController {

    @Autowired
    private AccountMoneyLogService accountMoneyLogService;
    @Autowired
    private BillHeadService billHeadService;

    //资金流水列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/log/index")
    @OperationLog(name = "资金流水")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CommonPageParam param) {
        return ApiResult.ok(accountMoneyLogService.index(param));
    }

    //单据详情
    @RequestMapping(value = "/billDetail", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/log/billDetail")
    @OperationLog(name = "单据详情")
    @ApiOperation(value = "billDetail", response = String.class)
    public ApiResult<BillHead> billDetail(Long billId) {
        return ApiResult.ok(billHeadService.billDetail(billId));
    }

}
