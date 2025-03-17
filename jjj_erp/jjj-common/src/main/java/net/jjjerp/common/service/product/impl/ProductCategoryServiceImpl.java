package net.jjjerp.common.service.product.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductCategory;
import net.jjjerp.common.mapper.product.ProductCategoryMapper;
import net.jjjerp.common.param.product.CategoryParam;
import net.jjjerp.common.service.product.ProductCategoryService;
import net.jjjerp.common.service.product.ProductService;
import net.jjjerp.common.vo.product.CategoryVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品类型表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

}
