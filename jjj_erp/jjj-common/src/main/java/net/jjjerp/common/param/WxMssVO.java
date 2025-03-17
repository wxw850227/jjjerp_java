package net.jjjerp.common.param;

import lombok.Data;

import java.util.Map;

@Data
public class WxMssVO {
    /**
     * 用户openid
     */
    private String touser;
    /**
     * 订阅消息模版id
     */
    private String template_id;
    /**
     * 默认跳到小程序首页
     */
    private String page;
    /**
     * 推送文字
     */
    private Map<String, TemplateData> data;
}
