

package net.jjjerp.admin.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.mapper.shop.ShopAccessMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.service.shop.ShopAccessService;
import org.springframework.stereotype.Service;


/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class ShopAccessServiceImpl extends BaseServiceImpl<ShopAccessMapper, AdminAccess> implements ShopAccessService {
}
