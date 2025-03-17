package net.jjjerp.admin.controller.setting.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.admin.param.shop.LoginLogPageParam;
import net.jjjerp.admin.service.shop.ShopLoginLogService;
import net.jjjerp.admin.vo.shop.LoginLogVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "setting", tags = {"登录日志"})
@RestController
@RequestMapping("/admin/setting/system/loginlog")
public class LoginLogController {

    @Autowired
    private ShopLoginLogService shopLoginLogService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/loginlog/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<LoginLogVo>> index(@RequestBody LoginLogPageParam loginLogPageParam) throws Exception{
        return ApiResult.ok(shopLoginLogService.getList(loginLogPageParam));
    }
}
