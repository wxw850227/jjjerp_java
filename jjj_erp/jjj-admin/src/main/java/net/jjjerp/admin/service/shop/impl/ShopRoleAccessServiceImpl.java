package net.jjjerp.admin.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminRoleAccess;
import net.jjjerp.common.mapper.shop.ShopRoleAccessMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.service.shop.ShopRoleAccessService;
import org.springframework.stereotype.Service;

/**
 * 商家用户角色权限关系表 服务实现类
 * @author jjjerp
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopRoleAccessServiceImpl extends BaseServiceImpl<ShopRoleAccessMapper, AdminRoleAccess> implements ShopRoleAccessService {
}
