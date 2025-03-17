package net.jjjerp.admin.service.app.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import net.jjjerp.common.entity.app.AppMp;
import net.jjjerp.common.entity.settings.Setting;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.mapper.app.AppMpMapper;
import net.jjjerp.common.mapper.settings.SettingMapper;
import net.jjjerp.common.settings.vo.StoreVo;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.service.app.AppMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信公众号记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppMpServiceImpl extends BaseServiceImpl<AppMpMapper, AppMp> implements AppMpService {
    @Lazy
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private SettingMapper settingMapper;
    @Autowired
    private SettingUtils settingUtils;


}
