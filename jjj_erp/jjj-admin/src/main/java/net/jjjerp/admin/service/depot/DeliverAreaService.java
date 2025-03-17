package net.jjjerp.admin.service.depot;

import net.jjjerp.common.entity.depot.DeliverArea;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 配送区域表 服务类
 *
 * @author jjjerp
 * @since 2024-04-29
 */
public interface DeliverAreaService extends BaseService<DeliverArea> {

    List<DeliverArea> getList();

    boolean add(DeliverArea area);

    boolean edit(DeliverArea area);

    boolean delById(Integer deliverAreaId);

    Map<String, Object> getAdd();

    Map<String, Object> getEdit(Integer deliverAreaId);
}
