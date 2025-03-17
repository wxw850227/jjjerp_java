package net.jjjerp.admin.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.AppOpen;
import net.jjjerp.common.mapper.app.AppOpenMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.admin.service.app.AppOpenService;
import org.springframework.stereotype.Service;

/**
 * app应用记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppOpenServiceImpl extends BaseServiceImpl<AppOpenMapper, AppOpen> implements AppOpenService {

    /**
     * 修改
     * @param openappId
     * @param openappSecret
     * @param logo
     * @return
     */
    public Boolean edit(String openappId, String openappSecret, String logo) {
        AppOpen app = this.getOne(new LambdaQueryWrapper<>());
        AppOpen updateBean = new AppOpen();
        updateBean.setOpenappId(openappId);
        updateBean.setOpenappSecret(openappSecret);
        updateBean.setLogo(logo);
        if (app == null) {
            return this.save(updateBean);
        } else {
            return this.update(updateBean, new LambdaQueryWrapper<>());
        }
    }

}
