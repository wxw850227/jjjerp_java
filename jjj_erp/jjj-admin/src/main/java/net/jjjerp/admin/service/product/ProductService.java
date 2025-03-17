package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.param.product.ProductParam;
import net.jjjerp.framework.common.service.BaseService;

import java.util.Map;

/**
 * 产品表 服务类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
public interface ProductService extends BaseService<Product> {

    Map<String,Object> getList(ProductPageParam productPageParam);

    Map<String,Object> getBaseData(Long productId);

    boolean add(ProductParam productParam);

    boolean edit(ProductParam productParam);

    boolean setState(String productId, Integer state);

    boolean setDelete(Long productId);

    String getMaxProductExtendCode();
}
