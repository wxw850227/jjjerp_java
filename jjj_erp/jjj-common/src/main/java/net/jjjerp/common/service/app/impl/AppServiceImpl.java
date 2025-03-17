package net.jjjerp.common.service.app.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.mapper.app.AppMapper;
import net.jjjerp.common.service.app.AppService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {

}
