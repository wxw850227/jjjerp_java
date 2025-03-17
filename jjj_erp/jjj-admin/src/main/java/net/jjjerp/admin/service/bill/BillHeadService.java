package net.jjjerp.admin.service.bill;

import net.jjjerp.common.entity.bill.BillAddress;
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

    Map<String, Object> indexOrder(CommonPageParam param);

    boolean addOrder(BillHead head);

    boolean editOrder(BillHead head);

    boolean closeOrder(Long billId, String closeReason);

    boolean auditOrder(BillHead head);

    boolean addOrderDelivery(BillHead head);

    Map<String, Object> bill(CommonPageParam param);

    //审核销售订单
    boolean auditMarketOrder(BillHead head);

    boolean repulseAudit(Long billId);

    boolean abnormal(BillHead head);

    boolean closeMarketOrder(Long billId, String closeReason);

    boolean picking(Long billId);

    boolean editDeliver(BillDelivery delivery);

    boolean deliver(Long billId);

    boolean recoverMarketOrder(Long billId);

    Map<String, Object> stockIndex(CommonPageParam param);

    List<ProductStockVo> depotIndex(CommonPageParam param);

    List<BillItem> getStockitemList(CommonPageParam param);

    List<BillItem> getFreezeitemList(CommonPageParam param);

    List<BillItem> getOpenningList(CommonPageParam param);

    boolean auditRefundOrder(BillHead head);

    Map<String, Object> scanIndex(CommonPageParam param);

    boolean refundManage(BillHead head);

    Paging<BillHead> customerIndex(CommonPageParam param);

    Map<String, Object> collectIndex(CommonPageParam param);

    BillHead transVo(BillHead head);

    BillHead billDetail(Long billId);

    Map<String, Object> jobBoard(CommonPageParam param);

    Map<String, Object> dealBoard(CommonPageParam param);

    Map<String, Object> stockBoard(CommonPageParam param);

    Map<String, Object> productBoard(CommonPageParam param);

    Map<String, Object> getHomeData(CommonPageParam param);

    Map<String, Object> getHomeDeal(CommonPageParam param);

    boolean editAddress(BillAddress address);

    boolean editRemark(BillHead head);
}
