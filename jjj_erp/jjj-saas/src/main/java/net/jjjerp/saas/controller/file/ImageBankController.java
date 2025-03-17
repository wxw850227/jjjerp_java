package net.jjjerp.saas.controller.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.ImageBankParam;
import net.jjjerp.saas.service.ImageBankService;
import net.jjjerp.common.param.ImageBankPageParam;
import net.jjjerp.common.util.ImageBankUtils;
import net.jjjerp.common.vo.image.ImageBankVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "image", tags = {"图片库列表"})
@RestController
@RequestMapping("/saas/image/bank")
public class ImageBankController {
    @Autowired
    private ImageBankService imageBankService;
    @Autowired
    private ImageBankUtils imageBankUtils;

    //一级分类列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<ImageBankVo>> index() throws Exception{
        return ApiResult.ok(imageBankUtils.index());
    }

    //新增图片
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated ImageBankParam categoryParam) {
        if(imageBankService.add(categoryParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    //新增一级分类
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @OperationLog(name = "addCategory")
    @ApiOperation(value = "addCategory", response = String.class)
    public ApiResult<String> addCategory(@RequestBody @Validated ImageBankParam categoryParam) {
        if(imageBankService.addCategory(categoryParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    //编辑
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated ImageBankParam categoryParam) {
        if(imageBankService.edit(categoryParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //删除
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer categoryId) {
        if(imageBankService.delById(categoryId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //批量删除
    @RequestMapping(value = "/deleteFiles", method = RequestMethod.POST)
    @OperationLog(name = "deleteFiles")
    @ApiOperation(value = "deleteFiles", response = String.class)
    public ApiResult<String> deleteFiles(String imageIds) throws Exception{
        if(imageBankService.deleteFiles(imageIds)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //分页列表
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @OperationLog(name = "list")
    @ApiOperation(value = "list", response = String.class)
    public ApiResult<Paging<ImageBankVo>> list(@RequestBody ImageBankPageParam param) throws Exception{
        return ApiResult.ok(imageBankUtils.list(param));
    }

    //移动图片
    @RequestMapping(value = "/moveFiles", method = RequestMethod.POST)
    @OperationLog(name = "moveFiles")
    @ApiOperation(value = "moveFiles", response = String.class)
    public ApiResult<String> moveFiles(Integer categoryId, String imageIds) throws Exception{
        if(imageBankService.moveFiles(categoryId, imageIds)) {
            return ApiResult.ok(null, "移动成功");
        }else{
            return ApiResult.fail("移动失败");
        }
    }
}
