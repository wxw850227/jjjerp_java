package net.jjjerp.common.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.customer.Customer;

import org.springframework.stereotype.Repository;


/**
 * 客户记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {


}
