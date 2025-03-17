import request from '@/utils/request'

const StockApi = {
//库存状况
  /*列表*/
  stockList(data, errorback) {
    return request._postBody('/admin/stock/condition/condition/index', data, errorback);
  },
  /*详情*/
   stockStockIndex(data, errorback) {
    return request._postBody('/admin/stock/condition/condition/stockIndex', data, errorback);
  },
   /*仓库详情*/
   stockDepotIndex(data, errorback) {
    return request._postBody('/admin/stock/condition/condition/depotIndex', data, errorback);
  },
  /*分布表*/
  stockDistribution(data, errorback) {
    return request._postBody('/admin/stock/condition/distribution/index', data, errorback);
  },
  /*期初库存*/
  stockOpenning(data, errorback) {
    return request._postBody('/admin/stock/condition/openning/index', data, errorback);
  },
  /*保存期初库存*/
  stockOpenningAdd(data, errorback) {
    return request._postBody('/admin/stock/condition/openning/add', data, errorback);
  },


//出入库
   /*库存调拨单*/
  stockAllotList(data, errorback) {
    return request._postBody('/admin/stock/allot/allocate/index', data, errorback);
  },
  /*新增*/
  stockAllotAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/allocate/add', data, errorback);
  },
  /*调拨出库*/
  stockAllocateOutList(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateOut/index', data, errorback);
  },
  /*待出库*/
 stockAllocateOutIndexOut(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateOut/indexOut', data, errorback);
  },
  /*新增*/
   stockAllocateOutAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateOut/add', data, errorback);
  },
  /*调拨入库*/
  stockAllocateInList(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateIn/index', data, errorback);
  },
  /*待入库*/
 stockAllocateIndexOut(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateIn/indexOut', data, errorback);
  },
  /*新增*/
   stockAllocateInAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/allocateIn/add', data, errorback);
  },

   /*其他出库列表*/
  stockAllotOtherOutList(data, errorback) {
    return request._postBody('/admin/stock/allot/otherOut/index', data, errorback);
  },
  /*新增其他出库单*/
  stockAllotOtherOutAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/otherOut/add', data, errorback);
  },
  /*快递修改*/
  stockOutEditDeliver(data, errorback) {
    return request._postBody('/admin/stock/allot/otherOut/editDeliver', data, errorback);
  },
  /*其他入库列表*/
  stockAllotOtherInList(data, errorback) {
    return request._postBody('/admin/stock/allot/otherIn/index', data, errorback);
  },
  /*新增其他出库单*/
  stockAllotOtherInAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/otherIn/add', data, errorback);
  },
    /*快递修改*/
  stockInEditDeliver(data, errorback) {
    return request._postBody('/admin/stock/allot/otherIn/editDeliver', data, errorback);
  },
  /*库存盘点列表*/
  stockAllotStockCheckList(data, errorback) {
    return request._postBody('/admin/stock/allot/stockCheck/index', data, errorback);
  },
    /*保存库存盘点*/
  stockStockCheckAdd(data, errorback) {
    return request._postBody('/admin/stock/allot/stockCheck/add', data, errorback);
  },
   /*出入库盘点查询*/
  stockStockRecordList(data, errorback) {
    return request._postBody('/admin/stock/allot/stockRecord/index', data, errorback);
  },
}

export default StockApi;
