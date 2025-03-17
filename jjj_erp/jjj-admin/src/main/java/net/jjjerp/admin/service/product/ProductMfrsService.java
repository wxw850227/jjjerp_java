package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 产品品牌表 服务类
 *
 * @author jjjerp
 * @since 2024-04-15
 */
public interface ProductMfrsService extends BaseService<ProductMfrs> {

    Paging<ProductMfrs> index(CommonPageParam param);

    boolean add(ProductMfrs mfrs);

    boolean edit(ProductMfrs mfrs);

    boolean delById(Integer id);

    List<ProductMfrs> getAll();
}
