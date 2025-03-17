package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 产品属性表 服务类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
public interface ProductAttributeService extends BaseService<ProductAttribute> {

    Paging<ProductAttribute> getproductAttribute(CommonPageParam param);

    boolean add(ProductAttribute param);

    ProductAttribute toEdit(Long id);

    boolean edit(ProductAttribute param);

    boolean setDelete(Long id);

    List<ProductAttribute> getAll();
}
