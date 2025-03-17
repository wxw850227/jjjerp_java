package net.jjjerp.common.service.bill;

import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.vo.product.ProductStockVo;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

import java.util.List;
import java.util.Map;

/**
 * 单据主表 服务类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
public interface BillHeadService extends BaseService<BillHead> {

}
