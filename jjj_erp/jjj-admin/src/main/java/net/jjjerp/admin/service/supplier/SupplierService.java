package net.jjjerp.admin.service.supplier;

import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;
import java.util.Map;

/**
 * 供应商/客户信息表 服务类
 *
 * @author jjjerp
 * @since 2024-04-18
 */
public interface SupplierService extends BaseService<Supplier> {

    Paging<Supplier> getList(CommonPageParam param);

    boolean add(Supplier supplier);

    boolean edit(Supplier supplier);

    boolean delById(Long supplierId);

    boolean setState(Long supplierId, Integer status);

    Map<String, Object> toEdit(Long supplierId);

    List<Supplier> getAll();
}
