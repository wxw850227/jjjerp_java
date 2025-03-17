package net.jjjerp.common.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.app.App;
import org.springframework.stereotype.Repository;

/**
 * 微信小程序记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Repository
public interface AppMapper extends BaseMapper<App> {

}
