

package net.jjjerp.saas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.RegionPageParam;
import net.jjjerp.saas.param.RegionParam;
import net.jjjerp.saas.service.RegionService;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.vo.RegionVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
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
@Api(value = "test", tags = {"地区管理"})
@RestController
@RequestMapping("/saas/region")
public class RegionController {

    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionCache regionCache;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody RegionPageParam regionPageParam) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", regionService.getList(regionPageParam));
        result.put("regionData", regionCache.getCacheTree());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer id) {
        if(regionService.setDelete(id)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<List<RegionVo>> toAdd() {
        return ApiResult.ok(regionCache.getCacheTree());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated RegionParam regionParam) {
        if(regionService.add(regionParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String,Object>> toEdit(Integer id) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("model", regionService.getEditData(id));
        result.put("regionData", regionCache.getCacheTree());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated RegionParam regionParam) {
        if(regionService.edit(regionParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
