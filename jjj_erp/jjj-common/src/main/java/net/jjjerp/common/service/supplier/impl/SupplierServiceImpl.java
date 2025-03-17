package net.jjjerp.common.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.mapper.supplier.SupplierMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.supplier.SupplierService;
import net.jjjerp.common.util.CodeUtils;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商/客户信息表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-18
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;
}
