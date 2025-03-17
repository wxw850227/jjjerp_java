package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.depot.StorageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 仓库货位表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotStorageService extends BaseService<DepotStorage> {

    Paging<DepotStorage> getList(CommonPageParam param);

    boolean add(DepotStorage storage);

    boolean edit(DepotStorage storage);

    boolean delById(Integer depotStorageId);

    boolean settingStock(DepotStorage storage);

    DepotStorage getStock(Integer depotStorageId);

    List<String> batchAdd(StorageParam param);
}
