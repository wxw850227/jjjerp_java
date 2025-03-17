package net.jjjerp.admin.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.shop.AdminAccess;
import net.jjjerp.common.entity.shop.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家用户记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-06-23
 */
@Repository
public interface ShopUserMapper extends BaseMapper<AdminUser> {
    List<AdminAccess> getAccessByUserId(@Param("userId") Integer userId);
}
