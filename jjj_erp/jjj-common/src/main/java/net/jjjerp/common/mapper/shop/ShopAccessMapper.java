package net.jjjerp.common.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.shop.AdminAccess;
import org.springframework.stereotype.Repository;

/**
 * 商家用户权限表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-06-22
 */
@Repository
public interface ShopAccessMapper extends BaseMapper<AdminAccess> {

}
