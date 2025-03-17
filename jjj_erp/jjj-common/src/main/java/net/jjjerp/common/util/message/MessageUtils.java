package net.jjjerp.common.util.message;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.mp.api.WxMpService;
import net.jjjerp.common.entity.settings.Message;
import net.jjjerp.common.entity.settings.MessageSettings;
import net.jjjerp.common.param.SignParam;
import net.jjjerp.common.service.settings.ExpressService;
import net.jjjerp.common.service.settings.MessageService;
import net.jjjerp.common.service.settings.MessageSettingsService;
import net.jjjerp.common.service.settings.RegionService;
import net.jjjerp.common.util.wx.AppMpUtils;
import net.jjjerp.framework.common.api.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.*;

@Slf4j
@Component
public class MessageUtils {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageSettingsService messageSettingsService;
    @Autowired
    private WxMessageUtils wxMessageUtils;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private MpMessageUtils mpMessageUtils;
    @Autowired
    private SmsMessageUtils smsMessageUtils;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private AppMpUtils appMpUtils;


    /**
     * 获取设置
     *
     * @param name
     * @return
     */
    private MessageSettings getSettings(String name, Integer appId) {
        Message message = messageService.getOne(new LambdaQueryWrapper<Message>().eq(Message::getMessageEname, name));
        return messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                .eq(MessageSettings::getMessageId, message.getMessageId()).eq(MessageSettings::getAppId, appId));
    }

    //获取签名和模版id
    public ApiResult<Map<String, Object>> getSign(SignParam signParam){
        Map<String, Object> result = new HashMap<>();
        // 如果来源是公众号, 则获取签名
        result.put("signPackage", this.getSignPackage(signParam));
        return ApiResult.ok(result);
    }

    /**
     * 获取模板ID
     */
    public List<String> getMessageByNameArr(String platform, String[] messageENameArr){
        List<String> templateArr = new ArrayList<>();
        //只适用于微信
        if("wx".equals(platform)){
            List<Message> list = messageService.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getWxStatus() == 1 && StringUtils.isNotBlank(settings.getWxTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getWxTemplate()).getString("templateId"));
                }
            }
        }
        //只适用于公众号
        if("mp".equals(platform)){
            List<Message> list = messageService.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getMpStatus() == 1 && StringUtils.isNotBlank(settings.getMpTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getMpTemplate()).getString("templateId"));
                }
            }
        }

        return templateArr;
    }

    //获取公众号签名
    public WxJsapiSignature getSignPackage(SignParam signParam) {
        //公众号签名
        WxJsapiSignature signPackage = null;
        // 公众号环境才生成签名
        if(StringUtils.isNotEmpty(signParam.getUrl()) && "mp".equals(signParam.getPaySource())){
            String transUrl = StringEscapeUtils.unescapeHtml4(signParam.getUrl());
            try {
                wxMpService.switchoverTo(appMpUtils.getConfig(wxMpService, signParam.getAppId()))
                        .getWxMpConfigStorage().isTicketExpired(TicketType.JSAPI);
                signPackage = wxMpService.createJsapiSignature(transUrl);
            } catch (Exception e){
                log.info("公众号签名异常:",e);
            }
        }
        return signPackage;
    }
}
