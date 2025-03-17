package net.jjjerp.common.util.message;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import net.jjjerp.common.util.wx.AppMpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MpMessageUtils {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private AppMpUtils appMpUtils;
    /**
     * 发送订阅消息
     */
    public void send(JSONObject data, String template, String openId, Long appId)
    {
        try{
            // 组装参数
            JSONObject params = JSONObject.parseObject(template);
            JSONObject varData = params.getJSONObject("varData");
            Map<String, String> msgDataList = new HashMap<>();
            Iterator iter = varData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                JSONObject value = varData.getJSONObject(key);
                // 如果模板包含变量
                if(data.containsKey(key)){
                    msgDataList.put(value.getString("fieldName"), getValueStr(value.getString("fieldName"),data.getString(key)));
                    //msgDataList.add(new WxMpTemplateData(value.getString("fieldName"), data.getString(key)));
                }else{
                    msgDataList.put(value.getString("fieldName"), getValueStr(value.getString("fieldName"),value.getString("fieldValue")));
                    //msgDataList.add(new WxMpTemplateData(value.getString("fieldName"), value.getString("fieldValue")));
                }
            }
            WxMpSubscribeMessage message = new WxMpSubscribeMessage();
            message.setDataMap(msgDataList);
            message.setUrl("");
            message.setToUser(openId);
            message.setTemplateId(params.getString("templateId"));
            wxMpService.switchoverTo(appMpUtils.getConfig(wxMpService, appId))
                    .getSubscribeMsgService().send(message);
        }catch (Exception e){
            log.info("微信公众号消息发送失败:",e);
        }
    }

    /**
     * 截取值，不能超过20字符
     * @return
     */
    private String getValueStr(String fieldName,String value){
        if(StringUtils.isEmpty(value)){
            return " ";
        }
        if(fieldName.contains("thing") &&  value.length() > 19){
            return value.substring(0,19);
        }
        return value;
    }
}
