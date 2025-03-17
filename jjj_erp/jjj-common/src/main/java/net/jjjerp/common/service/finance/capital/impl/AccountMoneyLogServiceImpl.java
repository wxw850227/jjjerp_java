package net.jjjerp.common.service.finance.capital.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.finance.capital.AccountMoneyLog;
import net.jjjerp.common.entity.settings.Express;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.mapper.finance.capital.AccountMoneyLogMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.bill.BillHeadService;
import net.jjjerp.common.service.customer.CustomerService;
import net.jjjerp.common.service.finance.capital.AccountMoneyLogService;
import net.jjjerp.common.service.finance.capital.AccountService;
import net.jjjerp.common.service.settings.ExpressService;
import net.jjjerp.common.service.store.StoreService;
import net.jjjerp.common.service.supplier.SupplierService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户资金变动明细表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-09
 */
@Slf4j
@Service
public class AccountMoneyLogServiceImpl extends BaseServiceImpl<AccountMoneyLogMapper, AccountMoneyLog> implements AccountMoneyLogService {

}
