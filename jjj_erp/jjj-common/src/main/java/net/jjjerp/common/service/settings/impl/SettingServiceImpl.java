package net.jjjerp.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.Setting;
import net.jjjerp.common.mapper.settings.SettingMapper;
import net.jjjerp.common.service.settings.SettingService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商城设置记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
@Slf4j
@Service
public class SettingServiceImpl extends BaseServiceImpl<SettingMapper, Setting> implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

}
