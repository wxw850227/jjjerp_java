package net.jjjerp.admin.controller.product.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.param.product.ProductParam;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.admin.service.product.ProductMfrsService;
import net.jjjerp.admin.service.product.ProductService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"商品管理"})
@RestController
@RequestMapping("/admin/product/base/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductExtendService productExtendService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ProductPageParam productPageParam) {
        return ApiResult.ok(productService.getList(productPageParam));
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("/product/base/product/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() {
        return ApiResult.ok(productService.getBaseData(0L));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated ProductParam productParam) {
        if(productService.add(productParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/product/base/product/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Long productId) {
        return ApiResult.ok(productService.getBaseData(productId));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated ProductParam productParam) {
        if(productService.edit(productParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //修改商品状态
    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/state")
    @OperationLog(name = "state")
    @ApiOperation(value = "state", response = String.class)
    public ApiResult<String> state(String productId, Integer enabled) {
        if(productService.setState(productId, enabled)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Long productId) {
        if(productService.setDelete(productId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }


    /**
     * 获取最大规格编码
     * @return
     */
    @GetMapping(value = "/getMaxProductCode")
    @ApiOperation(value = "获取最大规格编码")
    public ApiResult<String> getMaxProductCode() {
        return ApiResult.ok(productService.getMaxProductExtendCode(), "操作成功");
    }

    @RequestMapping(value = "/extendIndex", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/product/index")
    @OperationLog(name = "extendIndex")
    @ApiOperation(value = "extendIndex", response = String.class)
    public ApiResult<Paging<ProductExtend>> extendIndex(@Validated @RequestBody ProductPageParam productPageParam) {
        return ApiResult.ok(productExtendService.getList(productPageParam));
    }
}
