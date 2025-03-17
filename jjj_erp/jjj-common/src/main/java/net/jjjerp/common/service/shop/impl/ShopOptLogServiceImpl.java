package net.jjjerp.common.service.shop.impl;

import net.jjjerp.common.entity.shop.AdminOptLog;
import net.jjjerp.common.mapper.shop.ShopOptLogMapper;
import net.jjjerp.common.service.shop.ShopOptLogService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员操作记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopOptLogServiceImpl extends BaseServiceImpl<ShopOptLogMapper, AdminOptLog> implements ShopOptLogService {

    @Autowired
    private ShopOptLogMapper shopOptLogMapper;

}
