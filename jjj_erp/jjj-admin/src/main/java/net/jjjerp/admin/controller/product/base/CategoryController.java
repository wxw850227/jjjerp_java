package net.jjjerp.admin.controller.product.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.admin.service.product.ProductCategoryService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.common.param.product.CategoryParam;
import net.jjjerp.common.vo.product.CategoryVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "index", tags = {"商品分类列表"})
@RestController
@RequestMapping("/admin/product/base/category")
public class CategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    //查询分类列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/category/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<CategoryVo>> index(@RequestBody CategoryParam categoryParam) {
        return ApiResult.ok(productCategoryService.getList(categoryParam));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/category/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated CategoryParam categoryParam) {
        if(productCategoryService.add(categoryParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/category/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated CategoryParam categoryParam) {
        if(productCategoryService.edit(categoryParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/category/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer categoryId) {
        if(productCategoryService.delById(categoryId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //修改状态
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/category/set")
    @OperationLog(name = "set")
    @ApiOperation(value = "set", response = String.class)
    public ApiResult<String> set(Integer categoryId, Integer status) {
        if(productCategoryService.setState(categoryId, status)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
