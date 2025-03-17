package net.jjjerp.common.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.shop.AdminRole;

import org.springframework.stereotype.Repository;


/**
 * 商家用户角色表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-07-09
 */
@Repository
public interface ShopRoleMapper extends BaseMapper<AdminRole> {

}
