package net.jjjerp.admin.controller.product.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.product.ProductMfrsService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"品牌管理"})
@RestController
@RequestMapping("/admin/product/base/mfrs")
public class MfrsController {

    @Resource
    private ProductMfrsService mfrsService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/mfrs/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<ProductMfrs>> index(@RequestBody CommonPageParam param) {
        return ApiResult.ok(mfrsService.index(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/mfrs/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated ProductMfrs mfrs) {
        if(mfrsService.add(mfrs)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/mfrs/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated ProductMfrs mfrs) {
        if(mfrsService.edit(mfrs)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/mfrs/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer id){
        if(mfrsService.delById(id)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
