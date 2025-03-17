package net.jjjerp.saas.service;

import net.jjjerp.saas.param.FieldParam;
import net.jjjerp.common.entity.settings.MessageField;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 应用消息字段表 服务类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
public interface MessageFieldService extends BaseService<MessageField> {
    /**
     * 获取所有消息字段
     * @return
     */
    List<MessageField> getAll(Integer messageId);

    /**
     * 保存字段
     * @param fieldParam
     * @return
     */
    Boolean saveField(FieldParam fieldParam);

}
