package net.jjjerp.common.util;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.service.app.AppService;
import net.jjjerp.common.util.wx.WxPayUtils;
import net.jjjerp.config.properties.SpringBootJjjProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Component
public class PayUtils {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private AppService appService;

}
