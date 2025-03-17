package net.jjjerp.admin.service.customer;

import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.Map;

/**
 * 客户记录表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface CustomerService extends BaseService<Customer> {

    Paging<Customer> getList(CommonPageParam param);

    boolean add(Customer customer);

    Map<String, Object> toEdit(Integer userId);

    boolean edit(Customer customer);

    boolean delById(Integer userId);
}
