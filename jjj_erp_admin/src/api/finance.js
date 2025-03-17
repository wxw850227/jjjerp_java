import request from '@/utils/request'

let FinanceApi = {
    /*资金管理*/
      /*资金流水*/
    logList(data, errorback) {
        return request._postBody('/admin/finance/capital/log/index', data, errorback);
    },
    /*账户编辑*/
    logBillDetail(data, errorback) {
        return request._postBody('/admin/finance/capital/log/billDetail', data, errorback);
    },
    /*账户列表*/
    accountList(data, errorback) {
        return request._postBody('/admin/finance/capital/account/index', data, errorback);
    },
    /*账户新增*/
   accountAdd(data, errorback) {
        return request._postBody('/admin/finance/capital/account/add', data, errorback);
    },
    /*账户编辑*/
    accountEdit(data, errorback) {
        return request._postBody('/admin/finance/capital/account/edit', data, errorback);
    },
     /*账户删除*/
    accountDelete(data, errorback) {
        return request._post('/admin/finance/capital/account/delete', data, errorback);
    },
     /*账户状态*/
    accountState(data, errorback) {
        return request._post('/admin/finance/capital/account/state', data, errorback);
    },
       /*默认账户*/
    accountDefaultAccount(data, errorback) {
        return request._post('/admin/finance/capital/account/defaultAccount', data, errorback);
    },
      /*财务对账*/
    /*客户对账单*/
    customerList(data, errorback) {
        return request._postBody('/admin/finance/capital/customer/index', data, errorback);
    },
     /*账户汇总表*/
    collectList(data, errorback) {
        return request._postBody('/admin/finance/capital/collect/index', data, errorback);
    },
}

export default FinanceApi;
