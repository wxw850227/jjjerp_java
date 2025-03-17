package net.jjjerp.common.vo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.file.ImageBank;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("系统图片库VO")
public class ImageBankVo extends ImageBank {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类图片路径")
    private String image;

    @ApiModelProperty("子分类集合")
    private List<ImageBankVo> children;

    @ApiModelProperty("父类名称")
    private String parent;
}
