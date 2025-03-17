import request from '@/utils/request'

let StatementApi = {
  /*订单数据统计*/
  getBoardJob(data, errorback) {
    return request._postBody('/admin/statement/market/board/job', data, errorback);
  },
  getBoardDeal(data, errorback) {
    return request._postBody('/admin/statement/market/board/deal', data, errorback);
  },
   getBoardStock(data, errorback) {
    return request._postBody('/admin/statement/market/board/stock', data, errorback);
  },
  getBoardProduct(data, errorback) {
    return request._postBody('/admin/statement/market/board/product', data, errorback);
  },
}

export default StatementApi;
