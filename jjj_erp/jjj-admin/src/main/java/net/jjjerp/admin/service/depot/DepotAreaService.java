package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;

/**
 * 仓库区域表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotAreaService extends BaseService<DepotArea> {

    List<DepotArea> getList(CommonPageParam param);

    boolean add(DepotArea area);

    boolean edit(DepotArea area);

    boolean delById(Integer depotAreaId);
}
