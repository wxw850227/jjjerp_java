package net.jjjerp.common.service.product.impl;

import net.jjjerp.common.entity.product.ProductDeputyUnit;
import net.jjjerp.common.mapper.product.ProductDeputyUnitMapper;
import net.jjjerp.common.service.product.ProductDeputyUnitService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品副单位表 服务实现类
 *
 * @author jjjerp
 * @since 2024-06-27
 */
@Slf4j
@Service
public class ProductDeputyUnitServiceImpl extends BaseServiceImpl<ProductDeputyUnitMapper, ProductDeputyUnit> implements ProductDeputyUnitService {

    @Autowired
    private ProductDeputyUnitMapper productDeputyUnitMapper;

}
