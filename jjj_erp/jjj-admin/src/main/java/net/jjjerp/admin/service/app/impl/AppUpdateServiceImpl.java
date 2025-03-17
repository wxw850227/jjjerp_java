package net.jjjerp.admin.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.AppUpdate;
import net.jjjerp.common.mapper.app.AppUpdateMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.param.app.AppUpdateParam;
import net.jjjerp.admin.service.app.AppUpdateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * app升级记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppUpdateServiceImpl extends BaseServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {

    /**
     * 分页查询
     * @param
     * @return
     */
    public Paging<AppUpdate> getList(CommonPageParam commonPageParam) {
        return new Paging<>(this.page(new PageInfo<>(commonPageParam), new LambdaQueryWrapper<AppUpdate>().orderByDesc(AppUpdate::getCreateTime)));
    }

    /**
     * 添加
     * @param appUpdateParam
     * @return
     */
    public boolean add(AppUpdateParam appUpdateParam) {
        AppUpdate bean = new AppUpdate();
        BeanUtils.copyProperties(appUpdateParam, bean);
        return this.save(bean);
    }

    /**
     * 修改
     * @param appUpdateParam
     * @return
     */
    public boolean edit(AppUpdateParam appUpdateParam) {
        AppUpdate bean = new AppUpdate();
        BeanUtils.copyProperties(appUpdateParam, bean);
        return this.updateById(bean);
    }

    /**
     * 真删除
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        return this.removeById(id);
    }
}
