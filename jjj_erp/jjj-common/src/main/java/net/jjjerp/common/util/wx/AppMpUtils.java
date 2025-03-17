package net.jjjerp.common.util.wx;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import net.jjjerp.common.entity.app.AppMp;
import net.jjjerp.common.service.app.AppMpService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.core.util.RequestDetailThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 维信公众号工具类
 */
@Slf4j
@Configuration
public class AppMpUtils {

    @Lazy
    @Autowired
    private AppMpService appMpService;

    @Bean
    public WxMpConfigStorage wxMpConfig() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        // 临时放入，支付会再次设置
        config.setAppId("jjjerp");
        config.setSecret("jjjerp");
        return config;
    }


    @Bean
    public WxMpService wxMpService(WxMpConfigStorage maConfig) {
        WxMpService service = new WxMpServiceImpl();
        service.addConfigStorage(maConfig.getAppId(), maConfig);
        return service;
    }

    public String getConfig(WxMpService wxMpService, Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        AppMp appMp = appMpService.getById(appId);
        if(appMp == null || StringUtils.isEmpty(appMp.getMpappId())){
            throw new BusinessException("未设置微信公众号参数");
        }
        try{
            wxMpService.switchoverTo(appMp.getMpappId());
        }catch (Exception e){
            WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
            config.setAppId(appMp.getMpappId());
            config.setSecret(appMp.getMpappSecret());
            wxMpService.addConfigStorage(appMp.getMpappId(), config);
        }
        return appMp.getMpappId();
    }
}
