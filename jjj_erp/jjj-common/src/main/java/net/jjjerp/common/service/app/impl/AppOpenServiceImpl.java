package net.jjjerp.common.service.app.impl;

import net.jjjerp.common.entity.app.AppOpen;
import net.jjjerp.common.mapper.app.AppOpenMapper;
import net.jjjerp.common.service.app.AppOpenService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * app应用记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppOpenServiceImpl extends BaseServiceImpl<AppOpenMapper, AppOpen> implements AppOpenService {

    @Autowired
    private AppOpenMapper appOpenMapper;

}
