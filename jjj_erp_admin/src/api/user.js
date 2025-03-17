import request from '@/utils/request'

let UserApi = {

  /*用户登录*/
  login (data, errorback) {
    return request._post('/admin/passport/login', data, errorback);
  },
  saasLogin (data, errorback) {
    return request._post('/admin/passport/saasLogin', data, errorback);
  },
  /*退出登录*/
  loginOut (data, errorback) {
    return request._post('/admin/passport/logout', data, errorback);
  },
  /*添加用户*/
  adduser (data, errorback) {
    return request._post('/admin/user/user/add', data, errorback);
  },
  /*修改用户等级*/
  editgrade (data, errorback) {
    return request._postBody('/admin/user/user/editGrade', data, errorback);
  },
  /*修改用户*/
  editUser (data, errorback) {
    return request._postBody('/admin/user/user/edit', data, errorback);
  },
  /*充值*/
  userRecharge (data, errorback) {
    return request._postBody('/admin/user/user/recharge', data, errorback);
  },
  /*删除用户*/
  deleteuser (data, errorback) {
    return request._post('/admin/user/user/delete', data, errorback);
  },
  /*用户列表*/
  userlist (data, errorback) {
    return request._postBody('/admin/user/user/index', data, errorback);
  },
  /*等级列表*/
  gradelist (data, errorback) {
    return request._postBody('/admin/user/grade/index', data, errorback);
  },
  /*等级列表*/
  gradelog (data, errorback) {
    return request._postBody('/admin/user/grade/log', data, errorback);
  },
  /*添加等级*/
  addgrade (data, errorback) {
    return request._postBody('/admin/user/grade/add', data, errorback);
  },
  /*修改等级*/
  editGrade (data, errorback) {
    return request._postBody('/admin/user/grade/edit', data, errorback);
  },
  /*删除等级*/
  deletegrade (data, errorback) {
    return request._post('/admin/user/grade/delete', data, errorback);
  },
  /*用户余额*/
  BalanceLog (data, errorback) {
    return request._postBody('/admin/user/balance/index', data, errorback);
  },
  /*充值记录*/
  RechargeOrder (data, errorback) {
    return request._post('/admin/user.Recharge/order', data, errorback);
  },
  /*修改密码*/
  EditPass (data, errorback) {
    return request._post('/admin/passport/editPass', data, errorback);
  },
  /*用户标签*/
  toEditTag (data, errorback) {
    return request._get('/admin/user/user/toEditTag', data, errorback);
  },
  /*用户标签*/
  editTag (data, errorback) {
    return request._postBody('/admin/user/user/editTag', data, errorback);
  },
  /*用户详情*/
  getuser(data, errorback) {
    return request._get('/admin/user/user/toEdit', data, errorback);
  },
}

export default UserApi;
