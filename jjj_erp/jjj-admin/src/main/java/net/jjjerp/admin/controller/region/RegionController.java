

package net.jjjerp.admin.controller.region;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.vo.RegionVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "index", tags = {"地区管理"})
@RestController
@RequestMapping("/admin/region")
public class RegionController {

    @Autowired
    private RegionCache regionCache;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<RegionVo>> index() {
        return ApiResult.ok(regionCache.getCacheTree());
    }

}
