package net.jjjerp.admin.controller.product.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.admin.service.product.ProductAttributeService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.common.param.CommonPageParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jjjerp
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/product/base/attribute")
@Api(tags = {"商品属性"})
public class ProductAttributeController {

    @Resource
    private ProductAttributeService productAttributeService;

    /**
     * 获取商品属性列表
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ApiOperation(value = "获取商品属性列表")
    @RequiresPermissions("/product/base/attribute/index")
    @OperationLog(name = "index")
    public ApiResult<Map<String,Object>> index(@RequestBody CommonPageParam param) {
        Map<String,Object> result = new HashMap<>();
        result.put("attribute", productAttributeService.getproductAttribute(param));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/attribute/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody ProductAttribute param) {
        if(productAttributeService.add(param)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/product/base/attribute/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<ProductAttribute> toEdit(Long id) {
        return ApiResult.ok(productAttributeService.toEdit(id));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/attribute/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody ProductAttribute param) {
        if(productAttributeService.edit(param)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/base/attribute/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Long id) {
        if(productAttributeService.setDelete(id)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

}
