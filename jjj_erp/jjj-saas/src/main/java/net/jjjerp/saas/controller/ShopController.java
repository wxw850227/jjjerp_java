

package net.jjjerp.saas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.AppPageParam;
import net.jjjerp.saas.param.AppParam;
import net.jjjerp.saas.service.AppService;
import net.jjjerp.saas.vo.AppVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "test", tags = {"应用管理"})
@RestController
@RequestMapping("/saas/shop")
public class ShopController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<AppVo>> index(@Validated @RequestBody AppPageParam appPageParam) {
        return ApiResult.ok(appService.getList(appPageParam));
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @OperationLog(name = "updateStatus")
    @ApiOperation(value = "updateStatus", response = String.class)
    public ApiResult<String> updateStatus(Integer appId) {
        if(appService.editStatusById(appId)) {
            return ApiResult.ok(null, "更新成功");
        }else{
            return ApiResult.fail("更新失败");
        }
    }

    @RequestMapping(value = "/updateWxStatus", method = RequestMethod.POST)
    @OperationLog(name = "updateWxStatus")
    @ApiOperation(value = "updateWxStatus", response = String.class)
    public ApiResult<String> updateWxStatus(Integer appId) {
        if(appService.updateWxStatus(appId)) {
            return ApiResult.ok(null, "更新成功");
        }else{
            return ApiResult.fail("更新失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer appId) {
        if(appService.setDelete(appId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated AppParam appParam) {
        if(appService.add(appParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated AppParam appParam) {
        if(appService.edit(appParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
