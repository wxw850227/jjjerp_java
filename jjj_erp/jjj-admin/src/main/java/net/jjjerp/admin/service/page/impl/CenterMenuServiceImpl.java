package net.jjjerp.admin.service.page.impl;

import net.jjjerp.common.entity.page.CenterMenu;
import net.jjjerp.common.mapper.page.CenterMenuMapper;
import net.jjjerp.admin.service.page.CenterMenuService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 个人中心菜单 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-29
 */
@Slf4j
@Service
public class CenterMenuServiceImpl extends BaseServiceImpl<CenterMenuMapper, CenterMenu> implements CenterMenuService {

    @Autowired
    private CenterMenuMapper centerMenuMapper;

}
