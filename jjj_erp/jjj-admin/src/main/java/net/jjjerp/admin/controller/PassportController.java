

package net.jjjerp.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import net.jjjerp.framework.log.annotation.OperationLogIgnore;
import net.jjjerp.framework.shiro.util.JwtTokenUtil;
import net.jjjerp.admin.service.shop.ShopUserService;
import net.jjjerp.framework.shiro.vo.LoginShopUserTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(value = "login", tags = {"shop登录"})
@RestController
@RequestMapping("/admin/passport")
public class PassportController {

    @Autowired
    private ShopUserService shopUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(name = "login")
    @ApiOperation(value = "login", response = String.class)
    public ApiResult<LoginShopUserTokenVo> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        log.debug("username..."+username);
        log.debug("password..."+password);
        LoginShopUserTokenVo user = shopUserService.login(username, password);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName("admin"), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @PostMapping("/editPass")
    @OperationLogIgnore
    public ApiResult<String> editPass(String oldpass, String password, String confirmPass) throws Exception {
        if(shopUserService.renew(oldpass, password, confirmPass)){
            return ApiResult.ok("修改成功");
        }else{
            return ApiResult.ok("修改失败");
        }
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        shopUserService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
