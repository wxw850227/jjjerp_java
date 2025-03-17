package net.jjjerp.common.service.app.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.AppMp;
import net.jjjerp.common.mapper.app.AppMpMapper;
import net.jjjerp.common.service.app.AppMpService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 微信公众号记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppMpServiceImpl extends BaseServiceImpl<AppMpMapper, AppMp> implements AppMpService {


}
