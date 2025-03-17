package net.jjjerp.common.service.customer.impl;

import net.jjjerp.common.entity.customer.CustomerAddress;
import net.jjjerp.common.mapper.customer.CustomerAddressMapper;
import net.jjjerp.common.service.customer.CustomerAddressService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户收货地址表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class CustomerAddressServiceImpl extends BaseServiceImpl<CustomerAddressMapper, CustomerAddress> implements CustomerAddressService {

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

}
