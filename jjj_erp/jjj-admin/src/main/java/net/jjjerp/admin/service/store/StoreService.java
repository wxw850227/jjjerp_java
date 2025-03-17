package net.jjjerp.admin.service.store;

import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;
import java.util.Map;

/**
 * 供应商/客户信息表 服务类
 *
 * @author jjjerp
 * @since 2024-04-19
 */
public interface StoreService extends BaseService<Store> {

    Paging<Store> getList(CommonPageParam param);

    boolean add(Store store);

    Map<String, Object> toEdit(Long storeId);

    boolean edit(Store store);

    boolean delById(Long storeId);

    boolean setState(Long storeId, Integer enabled);

    List<Store> getAll();
}
