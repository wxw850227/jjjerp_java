package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.framework.common.service.BaseService;

/**
 * 仓库货位库存表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotStorageStockService extends BaseService<DepotStorageStock> {

    //检查商品规格是否已绑定
    Integer checkIsExtendExist(Long productExtendId, Integer storageStockId);

    //获取商品绑定货位
    DepotStorage getStorage(Long productExtendId);
}
