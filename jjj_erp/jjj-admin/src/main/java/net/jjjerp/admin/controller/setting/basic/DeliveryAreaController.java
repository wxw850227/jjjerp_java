

package net.jjjerp.admin.controller.setting.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.depot.DeliverArea;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DeliverAreaService;
import net.jjjerp.admin.service.depot.DepotAreaService;
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
import java.util.Map;

@Slf4j
@Api(value = "setting", tags = {"配送区域管理"})
@RestController
@RequestMapping("/admin/setting/basic/deliverArea")
public class DeliveryAreaController {

    @Autowired
    private DeliverAreaService areaService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/deliverArea/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<DeliverArea>> index() {
        return ApiResult.ok(areaService.getList());
    }

    //新增详情
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/deliverArea/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> getAdd() {
        return ApiResult.ok(areaService.getAdd());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/deliverArea/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated DeliverArea area) {
        if(areaService.add(area)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    //编辑详情
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/deliverArea/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> getEdit(Integer deliverAreaId) {
        return ApiResult.ok(areaService.getEdit(deliverAreaId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/deliverArea/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated DeliverArea area) {
        if(areaService.edit(area)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/deliverArea/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer deliverAreaId) {
        if(areaService.delById(deliverAreaId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
