package net.jjjerp.common.service.finance.capital.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.app.App;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.mapper.finance.capital.AccountMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.app.AppService;
import net.jjjerp.common.service.finance.capital.AccountService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.util.ShopLoginUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
