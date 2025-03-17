package net.jjjerp.common.service.page.impl;

import net.jjjerp.common.entity.page.Page;
import net.jjjerp.common.mapper.page.PageMapper;
import net.jjjerp.common.service.page.PageService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * diy页面表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-28
 */
@Slf4j
@Service
public class PageServiceImpl extends BaseServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private PageMapper pageMapper;

}
