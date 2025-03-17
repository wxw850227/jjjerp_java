package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 仓库库区表 服务类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
public interface DepotReservoirService extends BaseService<DepotReservoir> {

    List<DepotReservoir> getList(CommonPageParam param);

    boolean add(DepotReservoir reservoir);

    boolean edit(DepotReservoir reservoir);

    boolean delById(Integer depotReservoirId);
}
