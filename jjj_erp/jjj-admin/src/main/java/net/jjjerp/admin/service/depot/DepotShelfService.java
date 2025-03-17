package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 仓库货架表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotShelfService extends BaseService<DepotShelf> {

    List<DepotShelf> getList(CommonPageParam param);

    boolean add(DepotShelf shelf);

    boolean edit(DepotShelf shelf);

    boolean delById(Integer depotShelfId);
}
