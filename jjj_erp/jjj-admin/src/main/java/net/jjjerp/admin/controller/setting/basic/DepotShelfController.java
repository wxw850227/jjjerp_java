

package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DepotReservoirService;
import net.jjjerp.admin.service.depot.DepotShelfService;
import net.jjjerp.framework.common.api.ApiResult;
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
@Api(value = "setting", tags = {"仓库货架管理"})
@RestController
@RequestMapping("/admin/setting/basic/depotShelf")
public class DepotShelfController {

    @Autowired
    private DepotShelfService shelfService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotShelf/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<DepotShelf>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(shelfService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotShelf/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated DepotShelf shelf) {
        if(shelfService.add(shelf)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotShelf/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated DepotShelf shelf) {
        if(shelfService.edit(shelf)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotShelf/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer depotShelfId) {
        if(shelfService.delById(depotShelfId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
