

package net.jjjerp.common.param.product;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.product.ProductImage;
import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ProductParam对象", description = "Product新增修改参数")
public class ProductParam extends Product {

    @ApiModelProperty("图片集合")
    private List<ProductImage> image;

    @ApiModelProperty("场景，编辑还是复制")
    private String scene;

    @ApiModelProperty("商品sku")
    private List<ProductExtend> skuList;
}
