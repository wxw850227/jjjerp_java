

package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.enums.StoreTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.customer.CustomerService;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "setting", tags = {"客户管理"})
@RestController
@RequestMapping("/admin/setting/basic/user")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private RegionCache regionCache;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/user/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<Customer>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(customerService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/user/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated Customer customer) {
        if(customerService.add(customer)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/user/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() {
        Map<String, Object> map = new HashMap<>();
        map.put("region",regionCache.getCacheTree());
        map.put("platformType", StoreTypeEnum.getList());
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/user/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer userId) {
        return ApiResult.ok(customerService.toEdit(userId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/user/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated Customer customer) {
        if(customerService.edit(customer)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/user/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer userId) {
        if(customerService.delById(userId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
