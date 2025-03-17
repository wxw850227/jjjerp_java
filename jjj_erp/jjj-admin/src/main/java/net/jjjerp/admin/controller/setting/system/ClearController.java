

package net.jjjerp.admin.controller.setting.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.config.constant.CommonRedisKey;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.util.RequestDetailThreadLocal;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@Api(value = "clear", tags = {"清理缓存"})
@RestController
@RequestMapping("/admin/setting/system/clear")
public class ClearController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/system/clear/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index() {
        String allKey = String.format(CommonRedisKey.SETTING_DATA_ALL, RequestDetailThreadLocal.getRequestDetail().getAppId());
        Set<String> keysSet = redisTemplate.keys(allKey);
        redisTemplate.delete(keysSet);
        return ApiResult.ok(null, "清理成功");
    }
}
