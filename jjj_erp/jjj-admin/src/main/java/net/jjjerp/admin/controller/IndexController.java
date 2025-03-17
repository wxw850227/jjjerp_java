

package net.jjjerp.admin.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"首页"})
@RestController
@RequestMapping("/admin/index")
public class IndexController {
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private BillHeadService billHeadService;

    @RequestMapping(value = "/base", method = RequestMethod.POST)
    @OperationLog(name = "base")
    @ApiOperation(value = "base", response = String.class)
    public ApiResult<Map<String, String>> base() {
        JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), null);
        Map<String, String> result = new HashMap<String, String>();
        result.put("shopName", vo.getString("shopName"));
        result.put("shopBgImg", vo.getString("shopBgImg"));
        return ApiResult.ok(result);
    }

    //首页数据
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "首页数据")
    @ApiOperation(value = "首页数据", response = String.class)
    public ApiResult<Map<String, Object>> getHomeData(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.getHomeData(param));
    }

    //首页订单趋势
    @RequestMapping(value = "/deal", method = RequestMethod.POST)
    @OperationLog(name = "首页订单趋势")
    @ApiOperation(value = "首页订单趋势", response = String.class)
    public ApiResult<Map<String, Object>> getHomeDeal(@RequestBody CommonPageParam param) {
        return ApiResult.ok(billHeadService.getHomeDeal(param));
    }

    //登录设置
    @RequestMapping(value = "/loginSetting", method = RequestMethod.GET)
    @OperationLog(name = "loginSetting")
    @ApiOperation(value = "loginSetting", response = String.class)
    public ApiResult<JSONObject> loginSetting(){
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        return ApiResult.ok(vo);
    }
}
