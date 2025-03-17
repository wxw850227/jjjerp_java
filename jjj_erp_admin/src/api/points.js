import request from '@/utils/request'

let PointsApi = {
    /*积分设置*/
    setPoints(data, errorback) {
        return request._postBody('/shop/user/points/setting', data, errorback);
    },
    /*获取设置*/
    getPoints(data, errorback) {
        return request._get('/shop/user/points/setting', data, errorback);
    },
    /*获取积分列表*/
    GetUserList(data, errorback) {
        return request._postBody('/shop/user/points/log', data, errorback);
    },
}
export default PointsApi;
