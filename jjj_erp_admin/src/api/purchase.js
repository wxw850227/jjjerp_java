import request from '@/utils/request'

let PurchaseApi = {
  /*采购订单*/
  orderList(data, errorback) {
    return request._postBody('/admin/purchase/purchase/order/index', data, errorback);
  },
  orderAdd(data, errorback) {
    return request._postBody('/admin/purchase/purchase/order/add', data, errorback);
  },
   orderEdit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/order/edit', data, errorback);
  },
   orderClose(data, errorback) {
    return request._post('/admin/purchase/purchase/order/close', data, errorback);
  },
   orderAudit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/order/audit', data, errorback);
  },
   orderDelivery(data, errorback) {
    return request._postBody('/admin/purchase/purchase/order/delivery', data, errorback);
  },
  /*采购入库单 */
   storageList(data, errorback) {
    return request._postBody('/admin/purchase/purchase/storage/index', data, errorback);
  },
   storageBill(data, errorback) {
    return request._postBody('/admin/purchase/purchase/storage/bill', data, errorback);
  },
  storageAdd(data, errorback) {
    return request._postBody('/admin/purchase/purchase/storage/add', data, errorback);
  },
  storageEdit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/storage/edit', data, errorback);
  },
   storageClose(data, errorback) {
    return request._post('/admin/purchase/purchase/storage/close', data, errorback);
  },
   storageAudit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/storage/audit', data, errorback);
  },
   /*采购出库 */
   returnList(data, errorback) {
    return request._postBody('/admin/purchase/purchase/return/index', data, errorback);
  },
  returnAdd(data, errorback) {
    return request._postBody('/admin/purchase/purchase/return/add', data, errorback);
  },
  returnEdit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/return/edit', data, errorback);
  },
   returnAudit(data, errorback) {
    return request._postBody('/admin/purchase/purchase/return/audit', data, errorback);
  },
}
export default PurchaseApi;
