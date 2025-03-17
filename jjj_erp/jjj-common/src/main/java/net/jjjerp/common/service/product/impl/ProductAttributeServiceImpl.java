package net.jjjerp.common.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.mapper.product.ProductAttributeMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.product.ProductAttributeService;
import net.jjjerp.common.settings.vo.StoreVo;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品属性表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductAttributeServiceImpl extends BaseServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;



}
