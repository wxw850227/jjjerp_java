package net.jjjerp.common.service.app.impl;

import net.jjjerp.common.entity.app.AppWx;
import net.jjjerp.common.mapper.app.AppWxMapper;
import net.jjjerp.common.service.app.AppWxService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-03
 */
@Slf4j
@Service
public class AppWxServiceImpl extends BaseServiceImpl<AppWxMapper, AppWx> implements AppWxService {

    @Autowired
    private AppWxMapper appWxMapper;

}
