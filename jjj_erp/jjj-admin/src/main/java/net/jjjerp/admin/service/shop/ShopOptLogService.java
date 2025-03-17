package net.jjjerp.admin.service.shop;

import net.jjjerp.common.entity.shop.AdminOptLog;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.param.shop.OptLogPageParam;
import net.jjjerp.admin.vo.shop.OptLogVo;

/**
 * 管理员操作记录表 服务类
 * @author jjjerp
 * @since 2022-08-15
 */
public interface ShopOptLogService extends BaseService<AdminOptLog> {

    /**
     * 获取所有操作记录
     * @param optLogPageParam
     * @return
     */
    Paging<OptLogVo> getList(OptLogPageParam optLogPageParam);

}
