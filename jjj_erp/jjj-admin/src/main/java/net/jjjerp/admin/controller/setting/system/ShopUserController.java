

package net.jjjerp.admin.controller.setting.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.config.properties.SpringBootJjjProperties;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.framework.shiro.vo.LoginAdminAccessVo;
import net.jjjerp.framework.shiro.vo.LoginShopUserRedisVo;
import net.jjjerp.framework.util.ShopLoginUtil;
import net.jjjerp.admin.param.shopUser.ShopUserPageParam;
import net.jjjerp.admin.param.shopUser.ShopUserParam;
import net.jjjerp.admin.service.app.AppService;
import net.jjjerp.admin.service.shop.ShopRoleService;
import net.jjjerp.admin.service.shop.ShopUserService;
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
@Api(value = "index", tags = {"用户管理"})
@RestController
@RequestMapping("/admin/setting/system/user")
public class ShopUserController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private AppService appService;

    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    @OperationLog(name = "getRoleList")
    @ApiOperation(value = "getRoleList", response = String.class)
    public ApiResult<List<LoginAdminAccessVo>> getRoleList() throws Exception{
        LoginShopUserRedisVo loginShopUserRedisVo = ShopLoginUtil.getLoginShopUserRedisVo();
        return ApiResult.ok(loginShopUserRedisVo.getMenus());
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @OperationLog(name = "getUserInfo")
    @ApiOperation(value = "getUserInfo", response = String.class)
    public ApiResult<Map<String,Object>> getUserInfo() throws Exception{
        Map<String,Object> result = new HashMap<String,Object>();
        // 登录用户名
        result.put("username", ShopLoginUtil.getUsername());
        // 版本号
        result.put("version", springBootJjjProperties.getProjectVersion());
        // 商城名称
        App app = appService.getById(ShopLoginUtil.getAppId());
        if(app != null){
            result.put("shop_name", app.getAppName());
        }else {
            result.put("shop_name", "");
        }
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/user/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ShopUserPageParam params) throws Exception{
        Map<String,Object> result = new HashMap<>();
        result.put("userList", shopUserService.getList(params));
        result.put("roleList", shopRoleService.getList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/user/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody ShopUserParam shopUserParam) {
        return ApiResult.ok(shopUserService.add(shopUserParam));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/user/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody ShopUserParam shopUserParam) {
        return ApiResult.ok(shopUserService.edit(shopUserParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/user/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer shopUserId) {
        return ApiResult.ok(shopUserService.setDelete(shopUserId));
    }
}
