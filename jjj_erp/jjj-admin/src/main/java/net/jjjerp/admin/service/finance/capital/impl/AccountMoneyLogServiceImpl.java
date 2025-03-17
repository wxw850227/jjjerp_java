package net.jjjerp.admin.service.finance.capital.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.finance.capital.AccountMoneyLog;
import net.jjjerp.common.entity.settings.Express;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.mapper.finance.capital.AccountMoneyLogMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.customer.CustomerService;
import net.jjjerp.admin.service.finance.capital.AccountService;
import net.jjjerp.admin.service.settings.ExpressService;
import net.jjjerp.admin.service.store.StoreService;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.service.finance.capital.AccountMoneyLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private AccountMoneyLogMapper accountMoneyLogMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ExpressService expressService;

    //资金流水列表
    @Override
    public Map<String, Object> index(CommonPageParam param) {
        if(CollectionUtils.isEmpty(param.getTime()) || param.getTime().size() != 2){
            throw new BusinessException("查询日期不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        Page<AccountMoneyLog> page = new PageInfo(param);
        LambdaQueryWrapper<AccountMoneyLog> wrapper = new LambdaQueryWrapper<>();
        //店铺id
        if(param.getStoreId() != null && param.getStoreId() > 0){
            wrapper.eq(AccountMoneyLog::getStoreId, param.getStoreId());
        }
        //账户id
        if(param.getAccountId() != null && param.getAccountId() > 0){
            wrapper.eq(AccountMoneyLog::getAccountId, param.getAccountId());
        }
        //查询日期类型,0记账时间,1业务日期
        if(param.getTimeType() == 0){
            wrapper.between(AccountMoneyLog::getCreateTime, param.getTime().get(0) + " 00:00:00", param.getTime().get(1) + " 23:59:59");
        }else {
            wrapper.between(AccountMoneyLog::getBillTime, param.getTime().get(0) + " 00:00:00", param.getTime().get(1) + " 23:59:59");
        }
        //收支方向,1支出,2收入
        if(param.getDirectionType() != null && param.getDirectionType() > 0){
            wrapper.eq(AccountMoneyLog::getDirectionType, param.getDirectionType());
        }
        //交易往来单位,0其他,1客户,2供应商,3快递
        if(param.getTradingUnit() != null && param.getTradingUnit() > -1){
            wrapper.eq(AccountMoneyLog::getTradingUnit, param.getTradingUnit());
            switch (param.getTradingUnit()){
                case 1:
                    if(param.getCustomerId() != null && param.getCustomerId() > 0){
                        wrapper.eq(AccountMoneyLog::getCustomerId, param.getCustomerId());
                    }
                    break;
                case 2:
                    if(param.getSupplierId() != null && param.getSupplierId() > 0){
                        wrapper.eq(AccountMoneyLog::getSupplierId, param.getSupplierId());
                    }
                    break;
                case 3:
                    if(param.getExpressId() != null && param.getExpressId() > 0){
                        wrapper.eq(AccountMoneyLog::getExpressId, param.getExpressId());
                    }
                    break;
                default:
                    break;
            }
        }
        //单据编号
        if(StringUtils.isNotBlank(param.getCodeNum())){
            wrapper.like(AccountMoneyLog::getDescription, param.getCodeNum());
        }
        //单据类型
        if(param.getBillType() != null && param.getBillType() > 0){
            wrapper.eq(AccountMoneyLog::getBillType, param.getBillType());
        }
        IPage<AccountMoneyLog> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<AccountMoneyLog> resultPage = iPage.convert(this::transVo);
        map.put("log", new Paging<>(resultPage));
        //店铺列表
        map.put("store", storeService.getAll());
        //账户列表
        map.put("account", accountService.getAll());
        map.put("billType", BillTypeEnum.getList());
        return map;
    }

    //资金流水转换
    private AccountMoneyLog transVo(AccountMoneyLog accountMoneyLog) {
        if(accountMoneyLog.getStoreId() != null && accountMoneyLog.getStoreId() > 0){
            Store store = storeService.getById(accountMoneyLog.getStoreId());
            if(store != null){
                //店铺名称
                accountMoneyLog.setStoreName(store.getStoreName());
            }
        }
        //账户名称
        Account account = accountService.getById(accountMoneyLog.getAccountId());
        if(account != null){
            accountMoneyLog.setAccountName(account.getAccountName());
        }
        //单据类型名称
        accountMoneyLog.setTypeName(BillTypeEnum.getName(accountMoneyLog.getBillType()));
        //收支方向,1支出,2收入
        accountMoneyLog.setDirectionTypeName(accountMoneyLog.getDirectionType() == 1?"支出":"收入");
        //交易往来单位,0其他,1客户,2供应商,3快递
        switch (accountMoneyLog.getTradingUnit()){
            case 1:
                if(accountMoneyLog.getCustomerId() != null && accountMoneyLog.getCustomerId() > 0){
                    Customer customer = customerService.getById(accountMoneyLog.getCustomerId());
                    if(customer != null){
                        //客户名称
                        accountMoneyLog.setTradingUnitName(customer.getNickName());
                    }
                }
                break;
            case 2:
                if(accountMoneyLog.getSupplierId() != null && accountMoneyLog.getSupplierId() > 0){
                    Supplier supplier = supplierService.getById(accountMoneyLog.getSupplierId());
                    if(supplier != null){
                        //供应商名称
                        accountMoneyLog.setTradingUnitName(supplier.getSupplierName());
                    }
                }
                break;
            case 3:
                if(accountMoneyLog.getExpressId() != null && accountMoneyLog.getExpressId() > 0){
                    Express express = expressService.getById(accountMoneyLog.getExpressId());
                    if(express != null){
                        //快递公司名称
                        accountMoneyLog.setTradingUnitName(express.getExpressName());
                    }
                }
        }
        return accountMoneyLog;
    }
}
