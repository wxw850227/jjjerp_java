package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.common.vo.RegionVo;
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

import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"供应商管理"})
@RestController
@RequestMapping("/admin/setting/basic/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/supplier/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<Supplier>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(supplierService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/supplier/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated Supplier supplier) {
        if(supplierService.add(supplier)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/supplier/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Long supplierId) {
        return ApiResult.ok(supplierService.toEdit(supplierId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/supplier/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated Supplier supplier) {
        if(supplierService.edit(supplier)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/supplier/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Long supplierId) {
        if(supplierService.delById(supplierId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //修改状态
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/supplier/set")
    @OperationLog(name = "set")
    @ApiOperation(value = "set", response = String.class)
    public ApiResult<String> set(Long supplierId, Integer enabled) {
        if(supplierService.setState(supplierId, enabled)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
