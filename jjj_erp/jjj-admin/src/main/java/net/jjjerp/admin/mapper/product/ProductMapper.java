package net.jjjerp.admin.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.product.Product;
import org.springframework.stereotype.Repository;


/**
 * 产品表 Mapper 接口
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 获取最大商品编码
     * @return
     */
    String getMaxProductCode();

    /**
     * 获取最大规格编码
     * @return
     */
    String getMaxProductExtendCode();
}
