package net.jjjerp.common.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.shop.AdminUser;
import org.springframework.stereotype.Repository;

/**
 * 商家用户记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Repository
public interface ShopUserMapper extends BaseMapper<AdminUser> {
}
