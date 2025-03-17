package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 仓库表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotService extends BaseService<Depot> {

    Paging<Depot> getList(CommonPageParam param);

    boolean add(Depot depot);

    boolean edit(Depot depot);

    boolean setState(Long depotId, Integer enabled);

    boolean delById(Long depotId);

    List<Depot> getAll();
}
