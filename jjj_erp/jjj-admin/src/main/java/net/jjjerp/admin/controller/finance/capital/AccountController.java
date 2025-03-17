package net.jjjerp.admin.controller.finance.capital;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.finance.capital.AccountService;
import net.jjjerp.admin.service.product.ProductMfrsService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(value = "index", tags = {"账户管理"})
@RestController
@RequestMapping("/admin/finance/capital/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<Account>> index(@RequestBody CommonPageParam param) {
        return ApiResult.ok(accountService.index(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated Account account) {
        if(accountService.add(account)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated Account account) {
        if(accountService.edit(account)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //修改状态
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/state")
    @OperationLog(name = "state")
    @ApiOperation(value = "state", response = String.class)
    public ApiResult<String> state(Long accountId, Integer enabled) {
        if(accountService.setState(accountId, enabled)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //默认账户
    @RequestMapping(value = "/defaultAccount", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/defaultAccount")
    @OperationLog(name = "defaultAccount")
    @ApiOperation(value = "defaultAccount", response = String.class)
    public ApiResult<String> defaultAccount(Long accountId) {
        if(accountService.defaultAccount(accountId)) {
            return ApiResult.ok(null, "设置成功");
        }else{
            return ApiResult.fail("设置失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/finance/capital/account/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Long accountId){
        if(accountService.delById(accountId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
