

package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DepotAreaService;
import net.jjjerp.admin.service.depot.DepotService;
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
@Api(value = "setting", tags = {"仓库区域管理"})
@RestController
@RequestMapping("/admin/setting/basic/depotArea")
public class DepotAreaController {

    @Autowired
    private DepotAreaService areaService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotArea/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<DepotArea>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(areaService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotArea/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated DepotArea area) {
        if(areaService.add(area)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotArea/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated DepotArea area) {
        if(areaService.edit(area)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/depotArea/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer depotAreaId) {
        if(areaService.delById(depotAreaId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
