

package net.jjjerp.saas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.service.SaasUserService;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"admin修改密码"})
@RestController
@RequestMapping("/saas/user")
public class UserController {

    @Autowired
    private SaasUserService saasUserService;

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    @OperationLog(name = "renew")
    @ApiOperation(value = "renew", response = String.class)
    public ApiResult<String> renew(String pass) {
        if(saasUserService.renew(pass)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
