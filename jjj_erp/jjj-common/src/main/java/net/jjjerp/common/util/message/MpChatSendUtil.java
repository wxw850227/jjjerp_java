package net.jjjerp.common.util.message;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import net.jjjerp.common.entity.app.AppMp;
import net.jjjerp.common.param.TemplateData;
import net.jjjerp.common.param.WxMssVO;
import net.jjjerp.common.service.app.AppMpService;
import net.jjjerp.framework.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Slf4j
@Component
public class MpChatSendUtil {

    @Autowired
    private AppMpService appMpService;

    /**
     * 发送订阅消息
     */
    public void send(JSONObject data, String template, String openId, Long appId)
    {
        try{
            // 组装参数
            JSONObject params = JSONObject.parseObject(template);
            JSONObject varData = params.getJSONObject("varData");
            Map<String, TemplateData> msgData = new HashMap<>();
            Iterator iter = varData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                JSONObject value = varData.getJSONObject(key);
                // 如果模板包含变量
                if(data.containsKey(key)){
                    msgData.put(value.getString("fieldName"),new TemplateData(data.getString(key)));
                }else{
                    msgData.put(value.getString("fieldName"),new TemplateData(value.getString("fieldValue")));
                }
            }
            // 1、获取 接口调用凭证
            RestTemplate restTemplate = new RestTemplate();
            String url =  "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="  + this.getAccessToken(appId);
            //拼接推送的模版
            WxMssVO wxMssVo = new WxMssVO();
            //用户的openId
            wxMssVo.setTouser(openId);
            //订阅消息模板id
            wxMssVo.setTemplate_id(params.getString("templateId"));
            wxMssVo.setData(msgData);
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(url, wxMssVo, String.class);
        }catch (Exception e){
            log.info("微信公众号消息发送失败:",e);
        }
    }

    /**
     * 获取小程序token
     *
     * @return
     */
    public String getAccessToken(Long id) {
        AppMp appMp = appMpService.getById(id);
        if(appMp == null || StringUtils.isEmpty(appMp.getMpappId())){
            throw new BusinessException("未设置微信公众号参数");
        }
        String appId = appMp.getMpappId();
        String secret = appMp.getMpappSecret();
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "appid=" + appId + "&secret=" + secret + "&grant_type=client_credential";
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            return jsonObject.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public List<WxMpTemplate> getTemplate(Long appId) {
        String url =  "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="  + this.getAccessToken(appId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);
        return WxMpTemplate.fromJson(response.getBody());
    }
}

