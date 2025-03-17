package net.jjjerp.common.util.wx;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.auth.Verifier;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.entity.app.AppMp;
import net.jjjerp.common.entity.app.AppOpen;
import net.jjjerp.common.entity.app.AppWx;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.service.app.AppMpService;
import net.jjjerp.common.service.app.AppOpenService;
import net.jjjerp.common.service.app.AppService;
import net.jjjerp.common.service.app.AppWxService;
import net.jjjerp.common.settings.vo.SysConfigVo;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.core.util.RequestDetailThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 维信小程序工具类
 */
@Configuration
public class WxPayUtils {

    @Lazy
    @Autowired
    private AppService appService;
    @Lazy
    @Autowired
    private AppWxService appWxService;
    @Lazy
    @Autowired
    private AppMpService appMpService;
    @Lazy
    @Autowired
    private AppOpenService appOpenService;
    @Lazy
    @Autowired
    private SettingUtils settingUtils;

    @Bean
    public WxPayService wxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId("jjjerp");
        wxPayService.addConfig("jjjerp", payConfig);
        return wxPayService;
    }

    public String getConfig(WxPayService wxPayService, String paySource, Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        App app = appService.getById(appId);
        if(app == null || StringUtils.isEmpty(app.getMchid())){
            throw new BusinessException("未设置微信支付");
        }
        String mchIdKey = "";
        if ("mp".equals(paySource) || "h5".equals(paySource)) {
            mchIdKey = "mp";
        } else if ("wx".equals(paySource)) {
            mchIdKey = "wx";
        } else if ("android".equals(paySource) || "ios".equals(paySource)) {
            mchIdKey = "open";
        }
        boolean isServicePay = this.isServicePay(appId);

        mchIdKey = app.getMchid() + "_" + mchIdKey;
        //try{
            //wxPayService.switchoverTo(mchIdKey);
        //}catch (Exception e){
            WxPayConfig payConfig = new WxPayConfig();
            if(isServicePay){
                JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), 0L);
                SysConfigVo configVo = JSONObject.toJavaObject(vo, SysConfigVo.class);

                payConfig.setPrivateKeyContent(configVo.getWeixinService().getKeyPem().getBytes());
                payConfig.setPrivateCertContent(configVo.getWeixinService().getCertPem().getBytes());
                payConfig.setMchId(StringUtils.trimToNull(configVo.getWeixinService().getMchId()));
                payConfig.setMchKey(StringUtils.trimToNull(configVo.getWeixinService().getApiKey()));
                payConfig.setApiV3Key(StringUtils.trimToNull(configVo.getWeixinService().getApiKey()));
                payConfig.setSubMchId(StringUtils.trimToNull(app.getMchid()));
                payConfig.setAppId(StringUtils.trimToNull(configVo.getWeixinService().getAppId()));
            }else {
                payConfig.setMchId(StringUtils.trimToNull(app.getMchid()));
                payConfig.setMchKey(StringUtils.trimToNull(app.getApikey()));
                payConfig.setApiV3Key(StringUtils.trimToNull(app.getApikey()));
                payConfig.setSubAppId(null);
                payConfig.setSubMchId(null);
                payConfig.setKeyContent(app.getP12());
                payConfig.setPrivateKeyContent(app.getKeyPem()==null?null : app.getKeyPem().getBytes());
                payConfig.setPrivateCertContent(app.getCertPem()==null?null : app.getCertPem().getBytes());
            }
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            String payAppId = "";
            if ("mp".equals(paySource) || "h5".equals(paySource)) {
                AppMp appMp = appMpService.getById(appId);
                //payConfig.setAppId(StringUtils.trimToNull(appMp.getMpappId()));
                payAppId = StringUtils.trimToNull(appMp.getMpappId());
            } else if ("wx".equals(paySource)) {
                AppWx appWx = appWxService.getById(appId);
                //payConfig.setAppId(StringUtils.trimToNull(appWx.getWxappId()));
                payAppId = StringUtils.trimToNull(appWx.getWxappId());
            } else if ("android".equals(paySource) || "ios".equals(paySource)) {
                AppOpen appOpen = appOpenService.getById(appId);
                //payConfig.setAppId(StringUtils.trimToNull(appOpen.getOpenappId()));
                payAppId = StringUtils.trimToNull(appOpen.getOpenappId());
            }
            if(isServicePay){
                payConfig.setSubAppId(payAppId);
            }else{
                payConfig.setAppId(StringUtils.trimToNull(payAppId));
            }
            wxPayService.addConfig(mchIdKey, payConfig);
        // 如果没有开服务商支付,检查证书是否正确
            if(app.getWxPayKind() == 3  && !isServicePay){
                Verifier verifier = null;
                try {
                    verifier = payConfig.getVerifier();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new BusinessException("证书错误，请检查或重试");
                }
                String serialNo = verifier.getValidCertificate().getSerialNumber().toString(16).toUpperCase();
                if(StringUtils.isEmpty(serialNo)){
                    throw new BusinessException("证书错误，请检查或重试");
                }
                appService.update(new LambdaUpdateWrapper<App>().eq(App::getAppId,app.getAppId()).set(App::getWechatpaySerial, serialNo));
            }
        //}
        return mchIdKey;
    }

    public boolean isServicePay(Long appId){
        JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), 0L);
        SysConfigVo configVo = JSONObject.toJavaObject(vo, SysConfigVo.class);
        App app = appService.getById(appId);
        if(configVo.getWeixinService().getIsOpen() && app.getWeixinService()){
            return true;
        }
        return false;
    }
}
