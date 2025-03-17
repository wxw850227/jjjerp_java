import request from '@/utils/request'

let IndexApi = {

  /*基础配置*/
  base(data, errorback) {
    return request._post('/admin/index/base', data, errorback);
  },

  /*商城首页*/
  getCount(data, errorback) {
    return request._postBody('/admin/index/index', data, errorback);
  },
  getDeal(data, errorback) {
    return request._postBody('/admin/index/deal', data, errorback);
  },
}

export default IndexApi;
