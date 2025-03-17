import request from '@/utils/request'

const MarketApi = {
//审单
  /*订单列表*/
  marketOrderIndex(data, errorback) {
    return request._postBody('/admin/market/audit/order/index', data, errorback);
  },
  /*添加*/
  marketOrderAdd(data, errorback) {
    return request._postBody('/admin/market/audit/order/add', data, errorback);
  },
  /*审核*/
  marketOrderAudit(data, errorback) {
    return request._postBody('/admin/market/audit/order/audit', data, errorback);
  },
  /*编辑*/
  marketOrderEdit(data, errorback) {
    return request._postBody('/admin/market/audit/order/edit', data, errorback);
  },
  /*异常订单*/
  marketOrderAbnormal(data, errorback) {
    return request._postBody('/admin/market/audit/order/abnormal', data, errorback);
  },
    /*订单物流*/
  editDeliver(data, errorback) {
    return request._postBody('/admin/market/audit/order/editDeliver', data, errorback);
  },
//仓内拣货
   /*待配货订单列表*/
  pickingOrderIndex(data, errorback) {
    return request._postBody('/admin/market/picking/order/index', data, errorback);
  },
  /*打回审核*/
  pickingOrderRepulseAudit(data, errorback) {
    return request._post('/admin/market/audit/order/repulseAudit', data, errorback);
  },
  /*配货*/
  pickingOrderPicking(data, errorback) {
    return request._post('/admin/market/picking/order/picking', data, errorback);
  },
  /*异常订单关闭*/
  pickingOrderClose(data, errorback) {
    return request._postBody('/admin/market/picking/order/close', data, errorback);
  },
  //发货出库
   /*订单列表*/
  deliverOrderIndex(data, errorback) {
    return request._postBody('/admin/market/deliver/order/index', data, errorback);
  },
  /*出库单列表*/
  deliverOrderCkIndex(data, errorback) {
    return request._postBody('/admin/market/deliver/order/ckIndex', data, errorback);
  },
  /*新增线下出库单  */
   deliverOrderAdd(data, errorback) {
    return request._postBody('/admin/market/deliver/order/add', data, errorback);
  },
  /*确认发货*/
  deliverOrderDeliver(data, errorback) {
    return request._post('/admin/market/deliver/order/deliver', data, errorback);
  },
  /*编辑*/
  deliverOrderEdit(data, errorback) {
    return request._postBody('/admin/market/deliver/order/edit', data, errorback);
  },
   //其他
   /*订单查询*/
  otherOrderIndex(data, errorback) {
    return request._postBody('/admin/market/other/order/index', data, errorback);
  },
  /*已发货列表*/
  otherOrderDeliverIndex(data, errorback) {
    return request._postBody('/admin/market/other/order/deliverIndex', data, errorback);
  },
  /*异常订单列表*/
  otherOrderAbnormalIndexr(data, errorback) {
    return request._postBody('/admin/market/other/order/abnormalIndex', data, errorback);
  },
  /*编辑*/
  otherOrderEdit(data, errorback) {
    return request._postBody('/admin/market/other/order/edit', data, errorback);
  },
  /*异常订单关闭*/
 otherOrderClose(data, errorback) {
    return request._post('/admin/market/other/order/close', data, errorback);
  },
  otherOrderRecover(data, errorback) {
    return request._post('/admin/market/other/order/recover', data, errorback);
  },
  /*修改单据地址*/
  editAddress(data, errorback) {
    return request._postBody('/admin/bill/editAddress', data, errorback);
  },
  /*修改单据备注信息*/
  editRemark(data, errorback) {
    return request._postBody('/admin/bill/editRemark', data, errorback);
  },
}

export default MarketApi;
