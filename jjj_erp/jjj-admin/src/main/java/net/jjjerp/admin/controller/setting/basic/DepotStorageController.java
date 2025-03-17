

package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.depot.StorageParam;
import net.jjjerp.admin.service.depot.DepotShelfService;
import net.jjjerp.admin.service.depot.DepotStorageService;
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

@Slf4j
@Api(value = "setting", tags = {"仓库货位管理"})
@RestController
@RequestMapping("/admin/setting/basic/depotStorage")
public class DepotStorageController {

    @Autowired
    private DepotStorageService storageService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<DepotStorage>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(storageService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated DepotStorage storage) {
        if(storageService.add(storage)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated DepotStorage storage) {
        if(storageService.edit(storage)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer depotStorageId) {
        if(storageService.delById(depotStorageId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //货位库存详情
    @RequestMapping(value = "/settingStock", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/depotStorage/settingStock")
    @OperationLog(name = "settingStock")
    @ApiOperation(value = "settingStock", response = String.class)
    public ApiResult<DepotStorage> getStock(Integer depotStorageId) {
        return ApiResult.ok(storageService.getStock(depotStorageId));
    }

    //设置货位库存
    @RequestMapping(value = "/settingStock", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/settingStock")
    @OperationLog(name = "settingStock")
    @ApiOperation(value = "settingStock", response = String.class)
    public ApiResult<String> settingStock(@RequestBody DepotStorage storage) {
        if(storageService.settingStock(storage)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    //批量添加
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotStorage/batchAdd")
    @OperationLog(name = "batchAdd")
    @ApiOperation(value = "batchAdd", response = String.class)
    public ApiResult<List<String>> batchAdd(@RequestBody @Validated StorageParam param) {
        return ApiResult.ok(storageService.batchAdd(param));
    }
}
