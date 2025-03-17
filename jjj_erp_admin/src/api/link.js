import request from '@/utils/request'
let LinkApi = {
    /*获取数据列表*/
    getList(data, errorback) {
        return request._post('/admin/link/link/index', data, errorback);
    },
    /*获取数据列表*/
    getPageList(data, errorback) {
        return request._postBody('/admin/data/link/getPageList', data, errorback);
    },
}
export default LinkApi;
