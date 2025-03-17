import request from '@/utils/request'

let AccessApi = {
  /*商城列表*/
  shopList(data, errorback) {
    return request._postBody('/saas/shop/index', data, errorback);
  },
  /*添加商城*/
  addShop(data, errorback) {
    return request._postBody('/saas/shop/add', data, errorback);
  },
  /*修改商城*/
  editShop(data, errorback) {
    return request._postBody('/saas/shop/edit', data, errorback);
  },
  /*启用商城*/
  updateStatus(data, errorback) {
    return request._post('/saas/shop/updateStatus', data, errorback);
  },
  /*进入商城*/
  storeEnter(data, errorback) {
    return request._post('/saas/shop/enter', data, errorback);
  },
  /*删除商城*/
  deleteShop(data, errorback) {
    return request._post('/saas/shop/delete', data, errorback);
  },
  /*启用商城*/
  updateWxStatus(data, errorback) {
    return request._post('/saas/shop/updateWxStatus', data, errorback);
  },
}

export default AccessApi;
