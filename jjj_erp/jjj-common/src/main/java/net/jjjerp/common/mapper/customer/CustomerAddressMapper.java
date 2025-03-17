package net.jjjerp.common.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.customer.CustomerAddress;

import org.springframework.stereotype.Repository;


/**
 * 用户收货地址表 Mapper 接口
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Repository
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {


}
