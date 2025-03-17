package net.jjjerp.saas.service;

import net.jjjerp.saas.param.MessageParam;
import net.jjjerp.common.entity.settings.Message;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 应用消息表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
public interface MessageService extends BaseService<Message> {
    /**
     * 获取所有消息设置
     * @return
     */
    List<Message> getAll();
    /**
     * 删除
     * @param id
     * @return
     */
    Boolean setDelete(Integer id);
    /**
     * 新增
     * @param appParam
     * @return
     */
    Boolean add(MessageParam messageParam);
    /**
     * 修改
     * @param appParam
     * @return
     */
    Boolean edit(MessageParam messageParam);
}
