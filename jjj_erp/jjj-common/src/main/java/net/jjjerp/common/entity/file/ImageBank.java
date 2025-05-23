package net.jjjerp.common.entity.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统图库表
 *
 * @author jjjerp
 * @since 2023-10-31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjerp_image_bank")
@ApiModel(value = "ImageBank对象")
public class ImageBank implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图库id")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty("图库名称")
    private String name;

    @ApiModelProperty("上级图库id")
    private Integer parentId;

    @ApiModelProperty("图片路径")
    private String image;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
