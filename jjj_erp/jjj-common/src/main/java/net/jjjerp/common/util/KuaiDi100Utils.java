package net.jjjerp.common.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.service.settings.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KuaiDi100Utils {

    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ReturnAddressService returnAddressService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private DeliveryTemplateService deliveryTemplateService;
    @Autowired
    private LabelSettingService labelSettingService;


}
