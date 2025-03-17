

package net.jjjerp.common.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.mapper.shop.ShopUserMapper;
import net.jjjerp.common.service.shop.ShopUserService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class ShopUserServiceImpl extends BaseServiceImpl<ShopUserMapper, AdminUser> implements ShopUserService {

    @Autowired
    private ShopUserMapper shopUserMapper;
}
