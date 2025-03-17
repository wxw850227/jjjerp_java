package net.jjjerp.saas.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.service.ShopUserService;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.mapper.shop.ShopUserMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商家用户记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Slf4j
@Service
public class ShopUserServiceImpl extends BaseServiceImpl<ShopUserMapper, AdminUser> implements ShopUserService {


}
