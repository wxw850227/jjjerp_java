package net.jjjerp.admin.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.system.SystemVersion;
import net.jjjerp.common.mapper.system.SystemVersionMapper;
import net.jjjerp.admin.service.system.SystemVersionService;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统信息表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-22
 */
@Slf4j
@Service
public class SystemVersionServiceImpl extends BaseServiceImpl<SystemVersionMapper, SystemVersion> implements SystemVersionService {
    @Autowired
    private SystemVersionMapper systemVersionMapper;

    public String getVersion(){
        SystemVersion version = systemVersionMapper.selectOne(new LambdaQueryWrapper<SystemVersion>()
                .comment(CommonConstant.NOT_WITH_App_Id));
        return version.getVersionNo();
    }
}
