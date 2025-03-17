import request from "@/utils/request";

let DataApi = {
  /*用户接口*/
  getUser(data, errorback) {
    return request._postBody("/admin/data/user/lists", data, errorback);
  },
  /*用户接口*/
  getRegion(data, errorback) {
    return request._post('/admin/data/region/lists', data, errorback);
  },
};
export default DataApi;
