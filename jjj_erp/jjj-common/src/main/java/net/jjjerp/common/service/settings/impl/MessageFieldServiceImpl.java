package net.jjjerp.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.MessageField;
import net.jjjerp.common.mapper.settings.MessageFieldMapper;
import net.jjjerp.common.service.settings.MessageFieldService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 应用消息字段表 服务实现类
 * @author jjjerp
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageFieldServiceImpl extends BaseServiceImpl<MessageFieldMapper, MessageField> implements MessageFieldService {
}
