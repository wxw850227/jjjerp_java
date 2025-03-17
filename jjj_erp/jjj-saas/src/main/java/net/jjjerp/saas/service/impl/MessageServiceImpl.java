package net.jjjerp.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.MessageParam;
import net.jjjerp.saas.service.MessageService;
import net.jjjerp.common.entity.settings.Message;
import net.jjjerp.common.mapper.settings.MessageMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用消息表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 获取所有消息设置
     * @return
     */
    public List<Message> getAll(){
        LambdaQueryWrapper<Message> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Message::getIsDelete, false);
        // 获取所有已启用的权限列表
        return this.list(wrapper);
    }
    /**
     * 删除
     * @param id
     * @return
     */
    public Boolean setDelete(Integer id){
        Message updateBean = new Message();
        updateBean.setMessageId(id);
        updateBean.setIsDelete(true);
        return this.updateById(updateBean);
    }

    /**
     * 新增
     * @param messageParam
     * @return
     */
    public Boolean add(MessageParam messageParam){
        Message message = new Message();
        BeanUtils.copyProperties(messageParam, message);
        return this.save(message);
    }
    /**
     * 修改
     * @param messageParam
     * @return
     */
    public Boolean edit(MessageParam messageParam){
        Message message = new Message();
        BeanUtils.copyProperties(messageParam, message);
        return this.updateById(message);
    }
}
