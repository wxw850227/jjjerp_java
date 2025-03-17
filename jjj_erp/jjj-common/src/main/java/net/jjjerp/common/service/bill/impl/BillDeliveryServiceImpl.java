package net.jjjerp.common.service.bill.impl;

import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.mapper.bill.BillDeliveryMapper;
import net.jjjerp.common.service.bill.BillDeliveryService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单据物流表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Slf4j
@Service
public class BillDeliveryServiceImpl extends BaseServiceImpl<BillDeliveryMapper, BillDelivery> implements BillDeliveryService {

    @Autowired
    private BillDeliveryMapper billDeliveryMapper;

}
