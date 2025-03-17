package net.jjjerp.admin.controller.setting.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminRole;
import net.jjjerp.common.util.ShopAccessUtils;
import net.jjjerp.common.vo.shop.AdminAccessVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.admin.param.shopUser.ShopRoleParam;
import net.jjjerp.admin.service.shop.ShopRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"角色管理"})
@RestController
@RequestMapping("/admin/setting/system/role")
public class ShopRoleController {
    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private ShopAccessUtils shopAccessUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/role/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<AdminRole>> index() throws Exception{
        return ApiResult.ok(shopRoleService.getList());
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/setting/system/role/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<AdminAccessVo>> toAdd() {
        return ApiResult.ok(shopAccessUtils.getShopAll());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/role/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody ShopRoleParam shopRoleParam) {
        return ApiResult.ok(shopRoleService.add(shopRoleParam));
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/system/role/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String,Object>> toEdit(Integer roleId) {
        Map<String,Object> result = new HashMap<>();
        result.put("menu", shopAccessUtils.getShopAll());
        List<Integer> selectMenu = shopRoleService.getSelectList(roleId);
        result.put("selectMenu", selectMenu);
        result.put("model", shopRoleService.getById(roleId));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/role/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody ShopRoleParam shopRoleParam) {
        return ApiResult.ok(shopRoleService.edit(shopRoleParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/role/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer roleId) {
        return ApiResult.ok(shopRoleService.delete(roleId));
    }
}
