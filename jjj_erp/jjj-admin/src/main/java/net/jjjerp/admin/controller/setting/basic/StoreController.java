

package net.jjjerp.admin.controller.setting.basic;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.DeliveryTypeEnum;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.enums.StoreTypeEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.finance.capital.AccountService;
import net.jjjerp.admin.service.store.StoreService;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.common.util.SettingUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "setting", tags = {"店铺管理"})
@RestController
@RequestMapping("/admin/setting/basic/store")
public class StoreController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private RegionCache regionCache;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/store/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<Store>> index(@Validated @RequestBody CommonPageParam param) {
        return ApiResult.ok(storeService.getList(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/store/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated Store store) {
        if(storeService.add(store)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/store/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() {
        Map<String, Object> map = new HashMap<>();
        map.put("region",regionCache.getCacheTree());
        map.put("storeType", StoreTypeEnum.getList());
        map.put("account", accountService.getAll());
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/basic/store/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Long storeId) {
        return ApiResult.ok(storeService.toEdit(storeId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/store/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated Store store) {
        if(storeService.edit(store)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/store/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Long storeId) {
        if(storeService.delById(storeId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //修改状态
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @RequiresPermissions("/setting/basic/store/set")
    @OperationLog(name = "set")
    @ApiOperation(value = "set", response = String.class)
    public ApiResult<String> set(Long storeId, Integer enabled) {
        if(storeService.setState(storeId, enabled)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
