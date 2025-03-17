package net.jjjerp.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.common.entity.settings.LabelSetting;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("面单配置VO")
public class LabelSettingVo extends LabelSetting {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流公司名称")
    private String expressName;

}
