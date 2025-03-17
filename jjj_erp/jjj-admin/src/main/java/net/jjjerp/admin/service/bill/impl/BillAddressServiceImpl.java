package net.jjjerp.admin.service.bill.impl;

import net.jjjerp.common.entity.bill.BillAddress;
import net.jjjerp.common.mapper.bill.BillAddressMapper;
import net.jjjerp.admin.service.bill.BillAddressService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户收货地址表 服务实现类
 *
 * @author jjjerp
 * @since 2024-06-06
 */
@Slf4j
@Service
public class BillAddressServiceImpl extends BaseServiceImpl<BillAddressMapper, BillAddress> implements BillAddressService {

    @Autowired
    private BillAddressMapper billAddressMapper;

}
