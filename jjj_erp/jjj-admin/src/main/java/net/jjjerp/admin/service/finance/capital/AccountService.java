package net.jjjerp.admin.service.finance.capital;

import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 收支账户表 服务类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
public interface AccountService extends BaseService<Account> {
    Paging<Account> index(CommonPageParam param);

    boolean add(Account account);

    boolean edit(Account account);

    boolean delById(Long accountId);

    boolean setState(Long accountId, Integer enabled);

    boolean defaultAccount(Long accountId);

    List<Account> getAll();
}
