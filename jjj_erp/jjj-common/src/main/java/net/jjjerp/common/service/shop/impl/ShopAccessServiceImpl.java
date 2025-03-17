package net.jjjerp.common.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.mapper.shop.ShopAccessMapper;
import net.jjjerp.common.service.shop.ShopAccessService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商家用户权限表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-22
 */
@Slf4j
@Service
public class ShopAccessServiceImpl extends BaseServiceImpl<ShopAccessMapper, AdminAccess> implements ShopAccessService {

}
