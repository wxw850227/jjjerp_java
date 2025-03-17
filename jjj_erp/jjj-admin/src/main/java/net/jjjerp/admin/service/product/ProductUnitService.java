package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 多单位表 服务类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
public interface ProductUnitService extends BaseService<ProductUnit> {

    boolean batchSetStatus(Integer status, String ids);

    Paging<ProductUnit> index(CommonPageParam param);

    boolean add(ProductUnit productUnit);

    boolean edit(ProductUnit productUnit);

    boolean delById(String ids);

    List<ProductUnit> getAll();

    ProductUnit transVo(ProductUnit productUnit);
}
