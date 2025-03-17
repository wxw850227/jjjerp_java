package net.jjjerp.admin.service.product;

import net.jjjerp.common.entity.product.ProductCategory;
import net.jjjerp.common.param.product.CategoryParam;
import net.jjjerp.common.vo.product.CategoryVo;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 产品类型表 服务类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
public interface ProductCategoryService extends BaseService<ProductCategory> {

    /**分类列表
     * @return
     */
    List<CategoryVo> getAll();

    /**
     * 新增
     * @param categoryParam
     * @return
     */
    Boolean add(CategoryParam categoryParam);
    /**
     * 修改
     * @param categoryParam
     * @return
     */
    Boolean edit(CategoryParam categoryParam);
    /**
     * 真删除
     * @param id
     * @return
     */
    Boolean delById(Integer id);

    //修改状态
    boolean setState(Integer categoryId, Integer state);

    List<Integer> getSubCategoryId(Integer categoryId);

    List<CategoryVo> getList(CategoryParam categoryParam);
}
