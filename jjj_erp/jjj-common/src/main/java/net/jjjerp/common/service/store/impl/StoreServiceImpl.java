package net.jjjerp.common.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.StoreTypeEnum;
import net.jjjerp.common.mapper.store.StoreMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.bill.BillHeadService;
import net.jjjerp.common.service.finance.capital.AccountService;
import net.jjjerp.common.service.store.StoreService;
import net.jjjerp.common.util.CodeUtils;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商/客户信息表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-19
 */
@Slf4j
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    private StoreMapper storeMapper;
}
