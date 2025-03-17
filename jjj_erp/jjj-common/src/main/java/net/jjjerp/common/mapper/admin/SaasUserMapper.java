package net.jjjerp.common.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.admin.SaasUser;
import org.springframework.stereotype.Repository;

/**
 * 系统用户 Mapper 接口
 */
@Repository
public interface SaasUserMapper extends BaseMapper<SaasUser> {

}
