package net.jjjerp.admin.controller.setting.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.admin.param.shop.OptLogPageParam;
import net.jjjerp.admin.service.shop.ShopOptLogService;
import net.jjjerp.admin.vo.shop.OptLogVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "setting", tags = {"操作日志"})
@RestController
@RequestMapping("/admin/setting/system/optlog")
public class OptLogController {

    @Autowired
    private ShopOptLogService shopOptLogService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/optlog/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<OptLogVo>> index(@RequestBody OptLogPageParam optLogPageParam) throws Exception{
        return ApiResult.ok(shopOptLogService.getList(optLogPageParam));
    }
}
