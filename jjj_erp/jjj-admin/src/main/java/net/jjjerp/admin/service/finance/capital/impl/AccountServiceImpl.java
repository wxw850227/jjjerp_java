package net.jjjerp.admin.service.finance.capital.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.mapper.finance.capital.AccountMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.app.AppService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.util.ShopLoginUtil;
import net.jjjerp.admin.service.finance.capital.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收支账户表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Slf4j
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Account> implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AppService appService;

    @Override
    public Paging<Account> index(CommonPageParam commonPageParam) {
        Page<Account> page = new PageInfo(commonPageParam);
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getDeleteFlag, 0);
        wrapper.orderByDesc(Account::getCreateTime);
        IPage<Account> iPage = this.page(page, wrapper);
        App app = appService.getById(ShopLoginUtil.getAppId());
        // 最终返回分页对象
        IPage<Account> resultPage = iPage.convert(result -> {
            //是否默认，0不是，1是
            result.setIsDefault(0);
            if(app.getAccountId() != null && app.getAccountId().equals(result.getAccountId())){
                result.setIsDefault(1);
            }
            return result;
        });
        return new Paging(resultPage);
    }

    @Override
    public boolean add(Account account) {
        if(checkIsNameExist(null, account.getAccountName()) > 0){
            throw new BusinessException("账户名称不能重复");
        }
        account.setNowMoney(account.getOpeningMoney());
        this.save(account);
        App app = appService.getById(ShopLoginUtil.getAppId());
        //默认账户
        if(app.getAccountId() == null ){
            app.setAccountId(account.getAccountId());
            appService.updateById(app);
        }
        return true;
    }

    public int checkIsNameExist(Long id, String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Account::getAccountId, id);
        }
        wrapper.eq(Account::getAccountName, name);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(Account account) {
        if(checkIsNameExist(account.getAccountId(), account.getAccountName()) > 0){
            throw new BusinessException("账户名称不能重复");
        }
        return this.updateById(account);
    }

    @Override
    public boolean delById(Long accountId) {
        App app = appService.getById(ShopLoginUtil.getAppId());
        Long appAccountId = app.getAccountId();
        //默认账户不能删除
        if(appAccountId != null &&  appAccountId.equals(accountId)){
            throw new BusinessException("默认账户不能删除");
        }
        return this.removeById(accountId);
    }

    @Override
    public boolean setState(Long accountId, Integer enabled) {
        Account account = new Account();
        account.setAccountId(accountId);
        //启用 0-禁用  1-启用
        account.setEnabled(enabled);
        if(account.getEnabled() == 0){
            App app = appService.getById(ShopLoginUtil.getAppId());
            Long appAccountId = app.getAccountId();
            //默认账户不能删除
            if(appAccountId != null &&  appAccountId.equals(accountId)){
                throw new BusinessException("默认账户不能停用");
            }
        }
        return this.updateById(account);
    }

    @Override
    public boolean defaultAccount(Long accountId) {
        return appService.update(new LambdaUpdateWrapper<App>().eq(App::getAppId, ShopLoginUtil.getAppId())
                .set(App::getAccountId, accountId));
    }

    @Override
    public List<Account> getAll() {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getDeleteFlag, 0);
        wrapper.orderByDesc(Account::getCreateTime);
        List<Account> list = this.list( wrapper);
        return list;
    }
}
