package net.jjjerp.common.service.bill.impl;

import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.mapper.bill.BillItemMapper;
import net.jjjerp.common.service.bill.BillItemService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单据子表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Slf4j
@Service
public class BillItemServiceImpl extends BaseServiceImpl<BillItemMapper, BillItem> implements BillItemService {

    @Autowired
    private BillItemMapper billItemMapper;

}
