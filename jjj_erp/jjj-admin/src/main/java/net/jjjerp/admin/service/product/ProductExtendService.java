package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.vo.product.ProductSkuVo;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;
import java.util.Map;

/**
 * 产品价格扩展 服务类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
public interface ProductExtendService extends BaseService<ProductExtend> {

    List<ProductExtend> getSkuByProductId(Long productId);

    Paging<ProductExtend> getList(ProductPageParam productPageParam);

    ProductExtend transVo(ProductExtend extend);

    Map<String, Object> conditionIndex(CommonPageParam param);

    Map<String, Object> distributionIndex(CommonPageParam param);

    Map<String, Object> openningIndex(CommonPageParam param);

    Map<String, Object> checkIndex(CommonPageParam param);
}
