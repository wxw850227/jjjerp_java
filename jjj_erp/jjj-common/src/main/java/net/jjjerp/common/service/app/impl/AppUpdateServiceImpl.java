package net.jjjerp.common.service.app.impl;

import net.jjjerp.common.entity.app.AppUpdate;
import net.jjjerp.common.mapper.app.AppUpdateMapper;
import net.jjjerp.common.service.app.AppUpdateService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * app升级记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppUpdateServiceImpl extends BaseServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {

    @Autowired
    private AppUpdateMapper appUpdateMapper;

}
