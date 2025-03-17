package net.jjjerp.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.LabelSetting;
import net.jjjerp.common.mapper.settings.LabelSettingMapper;
import net.jjjerp.common.service.settings.LabelSettingService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 电子面单设置主表 服务实现类
 *
 * @author jjjerp
 * @since 2023-12-05
 */
@Slf4j
@Service
public class LabelSettingServiceImpl extends BaseServiceImpl<LabelSettingMapper, LabelSetting> implements LabelSettingService {


}
