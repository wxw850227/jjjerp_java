package net.jjjerp.admin.controller.product.base;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.CategoryParam;
import net.jjjerp.admin.service.product.ProductUnitService;
import net.jjjerp.common.vo.product.CategoryVo;
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
@Api(value = "index", tags = {"计量单位管理"})
@RestController
@RequestMapping("/admin/product/base/unit")
public class UnitController {

    @Resource
    private ProductUnitService unitService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/unit/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<ProductUnit>> index(@RequestBody CommonPageParam param) {
        return ApiResult.ok(unitService.index(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/unit/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated ProductUnit productUnit) {
        if(unitService.add(productUnit)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/unit/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated ProductUnit productUnit) {
        if(unitService.edit(productUnit)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/unit/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(String ids){
        if(unitService.delById(ids)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    /**
     * 批量设置状态-启用或者禁用
     * @return
     */
    @PostMapping(value = "/setStatus")
    @RequiresPermissions("/product/base/unit/setStatus")
    @OperationLog(name = "setStatus")
    @ApiOperation(value = "批量设置状态")
    public ApiResult<String> batchSetStatus(Integer state,String ids){
        if(unitService.batchSetStatus(state, ids)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }
}
