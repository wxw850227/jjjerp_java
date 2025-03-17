

package net.jjjerp.saas.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.v3.auth.Verifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.settings.vo.SysConfigVo;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"admin设置"})
@RestController
@RequestMapping("/saas/setting")
public class SettingController {

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), null);
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception{
        SysConfigVo sysConfigVo = JSONObject.toJavaObject(jsonData, SysConfigVo.class);
        boolean isOpen = sysConfigVo.getWeixinService().getIsOpen();
        if(isOpen){
            // 序列号
            String serialNo = this.getSerialNo(jsonData);
            jsonData.getJSONObject("weixinService").put("serialNo", serialNo);
        }
        settingUtils.saveSetting(SettingEnum.SYS_CONFIG.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }

    private String getSerialNo(JSONObject jsonData){
        SysConfigVo sysConfigVo = JSONObject.toJavaObject(jsonData, SysConfigVo.class);
        String serialNo = "";
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(StringUtils.trimToNull(sysConfigVo.getWeixinService().getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(sysConfigVo.getWeixinService().getApiKey()));
        payConfig.setApiV3Key(StringUtils.trimToNull(sysConfigVo.getWeixinService().getApiKey()));
        payConfig.setPrivateKeyContent(sysConfigVo.getWeixinService().getKeyPem().getBytes());
        payConfig.setPrivateCertContent(sysConfigVo.getWeixinService().getCertPem().getBytes());
        payConfig.setAppId(StringUtils.trimToNull(sysConfigVo.getWeixinService().getAppId()));
        Verifier verifier = payConfig.getVerifier();
        serialNo = verifier.getValidCertificate().getSerialNumber().toString(16).toUpperCase();
        if(StringUtils.isEmpty(serialNo)){
            throw new BusinessException("证书错误，请检查或重试");
        }
        return serialNo;
    }
}
