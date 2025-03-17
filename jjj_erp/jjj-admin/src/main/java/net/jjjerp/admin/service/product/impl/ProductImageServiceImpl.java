package net.jjjerp.admin.service.product.impl;

import net.jjjerp.common.entity.product.ProductImage;
import net.jjjerp.common.mapper.product.ProductImageMapper;
import net.jjjerp.admin.service.product.ProductImageService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品图片记录表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-16
 */
@Slf4j
@Service
public class ProductImageServiceImpl extends BaseServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {

    @Autowired
    private ProductImageMapper productImageMapper;

}
