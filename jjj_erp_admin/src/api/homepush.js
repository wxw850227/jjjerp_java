import request from '@/utils/request'

let HomepushApi = {
  /*获取数据*/
  getData(data, errorback) {
    return request._get('/admin/plus/homepush/index', data, errorback);
  },
  /*保存数据*/
  saveData(data, errorback) {
    return request._postBody('/admin/plus/homepush/index', data, errorback);
  },
}
export default HomepushApi;
