import request from '@/utils/request'

const RefundApi = {
//售后流程
  /*可退货列表*/
  refundFlowMarketIndex(data, errorback) {
    return request._postBody('/admin/refund/flow/refund/marketIndex', data, errorback);
  },
  /*新增*/
 refundFlowAdd(data, errorback) {
    return request._postBody('/admin/refund/flow/refund/add', data, errorback);
  },
   /*编辑*/
 refundFlowEdit(data, errorback) {
    return request._postBody('/admin/refund/flow/refund/edit', data, errorback);
  },
    /*编辑物流*/
 refundFlowEditDeliver(data, errorback) {
    return request._postBody('/admin/refund/flow/refund/editDeliver', data, errorback);
  },
//售后单审核
   /*待审核售后单*/
refundFlowAuditIndex(data, errorback) {
    return request._postBody('/admin/refund/flow/audit/index', data, errorback);
  },
  /*审核*/
 refundFlowAudit(data, errorback) {
    return request._postBody('/admin/refund/flow/audit/audit', data, errorback);
  },
  /*打回审核*/
 refundFlowRepulseAudit(data, errorback) {
    return request._post('/admin/refund/flow/audit/repulseAudit', data, errorback);
  },
 /*关闭售后*/
 refundFlowClose(data, errorback) {
    return request._post('/admin/refund/flow/audit/close', data, errorback);
  },
 

   /*已审核售后单*/
  refundFlowHandleIndex(data, errorback) {
    return request._postBody('/admin/refund/flow/handle/index', data, errorback);
  },
  /*入库*/
   refundFlowHandlePut(data, errorback) {
    return request._postBody('/admin/refund/flow/handle/put', data, errorback);
  },
  /*下单*/
  refundFlowHandleOrder(data, errorback) {
    return request._postBody('/admin/refund/flow/handle/order', data, errorback);
  },
 
   /*售后单列表*/
  refundFlowQueryList(data, errorback) {
    return request._postBody('/admin/refund/flow/query/index', data, errorback);
  },
   /*扫描入库*/
  refundPutScan(data, errorback) {
    return request._postBody('/admin/refund/put/scan/index', data, errorback);
  },
    /*原路返回*/
  refundPutRoad(data, errorback) {
    return request._postBody('/admin/refund/put/road/index', data, errorback);
  },
    /*销售退货入库*/
  refundPutMarket(data, errorback) {
    return request._postBody('/admin/refund/put/market/index', data, errorback);
  },
}

export default RefundApi;
