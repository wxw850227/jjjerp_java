package net.jjjerp.common.entity.product;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 商品图片记录表
 *
 * @author jjjerp
 * @since 2024-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_product_image")
@ApiModel(value = "ProductImage对象")
public class ProductImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("图片id(关联文件记录表)")
    private Integer imageId;

    @ApiModelProperty("商品图片主图")
    private String filePath;

    @ApiModelProperty("图片类型0商品主图，1详情图")
    private Integer imageType;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
