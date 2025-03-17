package net.jjjerp.admin.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.admin.service.product.ProductDeputyUnitService;
import net.jjjerp.common.entity.product.ProductDeputyUnit;
import net.jjjerp.common.mapper.product.ProductDeputyUnitMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
