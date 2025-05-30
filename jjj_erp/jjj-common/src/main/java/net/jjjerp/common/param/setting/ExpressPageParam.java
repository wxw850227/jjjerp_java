package net.jjjerp.common.param.setting;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjerp.framework.core.pagination.BasePageOrderParam;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "物流公司分页查询参数", description = "物流公司分页查询参数")
public class ExpressPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
