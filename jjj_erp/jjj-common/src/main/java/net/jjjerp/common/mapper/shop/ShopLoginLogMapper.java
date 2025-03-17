package net.jjjerp.common.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.shop.AdminLoginLog;

import org.springframework.stereotype.Repository;


/**
 * 管理员登录记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-08-15
 */
@Repository
public interface ShopLoginLogMapper extends BaseMapper<AdminLoginLog> {


}
