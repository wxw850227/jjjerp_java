package net.jjjerp.common.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.DeliveryTemplate;
import net.jjjerp.common.mapper.settings.DeliveryTemplateMapper;
import net.jjjerp.common.service.settings.DeliveryTemplateService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电子面单模板 服务实现类
 * @author jjjerp
 * @since 2022-06-29
 */
@Slf4j
@Service
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplateMapper, DeliveryTemplate> implements DeliveryTemplateService {


}
