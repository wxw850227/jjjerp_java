

package net.jjjerp.saas.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.validator.groups.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统图库 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ImageBankParam对象", description = "ImageBank新增修改参数")
public class ImageBankParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("主键id")
    private Integer categoryId;

    @ApiModelProperty("上级图库id")
    private Integer parentId;

    @NotEmpty(message = "名称不能为空")
    @ApiModelProperty("图库名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("图片路径")
    private String image;
}
