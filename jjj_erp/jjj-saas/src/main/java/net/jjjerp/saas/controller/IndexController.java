

package net.jjjerp.saas.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.config.properties.SpringBootJjjProperties;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@Api(value = "index", tags = {"版本号"})
@RestController
@RequestMapping("/saas/index")
public class IndexController {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index() throws IOException {
        log.debug("index...");
        return ApiResult.ok(springBootJjjProperties.getProjectVersion());
    }

}
