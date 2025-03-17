package net.jjjerp.common.service.shop.impl;

import net.jjjerp.common.entity.shop.AdminLoginLog;
import net.jjjerp.common.mapper.shop.ShopLoginLogMapper;
import net.jjjerp.common.service.shop.ShopLoginLogService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员登录记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopLoginLogServiceImpl extends BaseServiceImpl<ShopLoginLogMapper, AdminLoginLog> implements ShopLoginLogService {

    @Autowired
    private ShopLoginLogMapper shopLoginLogMapper;

}
