package net.jjjerp.admin.service.finance.capital;

import net.jjjerp.common.entity.finance.capital.AccountMoneyLog;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;

import java.util.Map;

/**
 * 账户资金变动明细表 服务类
 *
 * @author jjjerp
 * @since 2024-05-09
 */
public interface AccountMoneyLogService extends BaseService<AccountMoneyLog> {

    Map<String, Object> index(CommonPageParam param);
}
