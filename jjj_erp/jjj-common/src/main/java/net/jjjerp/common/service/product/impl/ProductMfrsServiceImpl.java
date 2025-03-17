package net.jjjerp.common.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.mapper.product.ProductMfrsMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.product.ProductMfrsService;
import net.jjjerp.common.util.StringUtil;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 产品品牌表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-15
 */
@Slf4j
@Service
public class ProductMfrsServiceImpl extends BaseServiceImpl<ProductMfrsMapper, ProductMfrs> implements ProductMfrsService {

    @Autowired
    private ProductMfrsMapper productMfrsMapper;

}
