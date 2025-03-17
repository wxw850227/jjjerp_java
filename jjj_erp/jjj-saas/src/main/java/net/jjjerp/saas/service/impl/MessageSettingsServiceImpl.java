package net.jjjerp.saas.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.service.MessageSettingsService;
import net.jjjerp.common.entity.settings.MessageSettings;
import net.jjjerp.common.mapper.settings.MessageSettingsMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用消息设置表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageSettingsServiceImpl extends BaseServiceImpl<MessageSettingsMapper, MessageSettings> implements MessageSettingsService {

    @Autowired
    private MessageSettingsMapper messageSettingsMapper;

}
