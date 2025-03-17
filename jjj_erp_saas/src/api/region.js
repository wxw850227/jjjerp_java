import request from '@/utils/request'

let RegionApi = {
  /*消息列表*/
  regionList(data, errorback) {
    return request._postBody('/saas/region/index', data, errorback);
  },
  toAddRegion(data, errorback) {
    return request._get('/saas/region/add', data, errorback);
  },
  /*添加消息*/
  addRegion(data, errorback) {
    return request._postBody('/saas/region/add', data, errorback);
  },
  /*物流公司详情*/
  regionDetail(data, errorback) {
    return request._get('/saas/region/edit', data, errorback);
  },
  /*修改消息*/
  editRegion(data, errorback) {
    return request._postBody('/saas/region/edit', data, errorback);
  },
  /*删除消息*/
  deleteRegion(data, errorback) {
    return request._post('/saas/region/delete', data, errorback);
  },
}

export default RegionApi;
