package net.jjjerp.common.service.customer.impl;


import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.mapper.customer.CustomerMapper;
import net.jjjerp.common.service.customer.CustomerService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 客户记录表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements CustomerService {


}
