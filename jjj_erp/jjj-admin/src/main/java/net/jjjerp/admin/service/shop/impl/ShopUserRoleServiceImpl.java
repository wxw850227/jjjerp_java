package net.jjjerp.admin.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.shop.AdminUserRole;
import net.jjjerp.common.mapper.shop.ShopUserRoleMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.service.shop.ShopUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 商家用户角色记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopUserRoleServiceImpl extends BaseServiceImpl<ShopUserRoleMapper, AdminUserRole> implements ShopUserRoleService {
}
