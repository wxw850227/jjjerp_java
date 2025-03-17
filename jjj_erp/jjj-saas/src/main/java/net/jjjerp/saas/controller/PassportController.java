

package net.jjjerp.saas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.service.SaasUserService;
import net.jjjerp.saas.vo.LoginAdminUserTokenVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.framework.log.annotation.OperationLogIgnore;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(value = "login", tags = {"admin登录"})
@RestController
@RequestMapping("/saas/passport")
public class PassportController {

    @Autowired
    private SaasUserService saasUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(name = "login")
    @ApiOperation(value = "login", response = String.class)
    public ApiResult<LoginAdminUserTokenVo> login(@RequestParam String username,@RequestParam String password, HttpServletResponse response) throws IOException {
        log.debug("username..."+username);
        log.debug("password..."+password);
        LoginAdminUserTokenVo user = saasUserService.login(username, password);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName("saas"), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        saasUserService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
