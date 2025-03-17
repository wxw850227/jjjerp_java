import request from '@/utils/request'

const SettingApi = {
   /*商城设置模板变量*/
  storeDetail(data, errorback) {
    return request._get('/admin/setting/system/system/index', data, errorback);
  },
  /*保存商城设置*/
  editStore(data, errorback) {
    return request._postBody('/admin/setting/system/system/index', data, errorback);
  },
    /*上传设置模板变量*/
  storageDetail(data, errorback) {
    return request._get('/admin/setting/system/storage/index', data, errorback);
  },
  //物流
   expressList(data, errorback) {
    return request._postBody('/admin/setting/delivery/express/index', data, errorback);
  },
   expressAdd(data, errorback) {
    return request._postBody('/admin/setting/delivery/express/add', data, errorback);
  },
   expressEdit(data, errorback) {
    return request._postBody('/admin/setting/delivery/express/edit', data, errorback);
  },
   expressDelete(data, errorback) {
    return request._post('/admin/setting/delivery/express/delete', data, errorback);
  },
  /*保存上传设置*/
  editStorage(data, errorback) {
    return request._postBody('/admin/setting/system/storage/index', data, errorback);
  },
  regionList(data, errorback) {
    return request._get('/admin/region/index', data, errorback);
  },
  /*供应商管理*/
  supplierList(data, errorback) {
    return request._postBody('/admin/setting/basic/supplier/index', data, errorback);
  },
  supplierAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/supplier/add', data, errorback);
  },
  supplierEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/supplier/edit', data, errorback);
  },
  supplierDel(data, errorback) {
    return request._post('/admin/setting/basic/supplier/delete', data, errorback);
  },
  supplierSet(data, errorback) {
    return request._post('/admin/setting/basic/supplier/set', data, errorback);
  },

  /*店铺管理*/
  storeList(data, errorback) {
    return request._postBody('/admin/setting/basic/store/index', data, errorback);
  },
  storeAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/store/add', data, errorback);
  },
  storeToAdd(data, errorback) {
    return request._get('/admin/setting/basic/store/add', data, errorback);
  },
  storeEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/store/edit', data, errorback);
  },
  storeToEdit(data, errorback) {
    return request._get('/admin/setting/basic/store/edit', data, errorback);
  },
  storeDel(data, errorback) {
    return request._post('/admin/setting/basic/store/delete', data, errorback);
  },
  storeSet(data, errorback) {
    return request._post('/admin/setting/basic/store/set', data, errorback);
  },

  /*客户管理*/
  userList(data, errorback) {
    return request._postBody('/admin/setting/basic/user/index', data, errorback);
  },
  userAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/user/add', data, errorback);
  },
  userToAdd(data, errorback) {
    return request._get('/admin/setting/basic/user/add', data, errorback);
  },
  userEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/user/edit', data, errorback);
  },
  userToEdit(data, errorback) {
    return request._get('/admin/setting/basic/user/edit', data, errorback);
  },
  userDel(data, errorback) {
    return request._post('/admin/setting/basic/user/delete', data, errorback);
  },


  /*仓库*/
  depotList(data, errorback) {
    return request._postBody('/admin/setting/basic/depot/index', data, errorback);
  },
  depotAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depot/add', data, errorback);
  },
  depotEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/depot/edit', data, errorback);
  },
  depotDel(data, errorback) {
    return request._post('/admin/setting/basic/depot/delete', data, errorback);
  },
  depotSet(data, errorback) {
    return request._post('/admin/setting/basic/depot/set', data, errorback);
  },
  /*仓库区域*/
  depotAreaList(data, errorback) {
    return request._postBody('/admin/setting/basic/depotArea/index', data, errorback);
  },
  depotAreaAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depotArea/add', data, errorback);
  },
  depotAreaEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/depotArea/edit', data, errorback);
  },
  depotAreaDel(data, errorback) {
    return request._post('/admin/setting/basic/depotArea/delete', data, errorback);
  },
  /*仓库库区*/
  depotReservoirList(data, errorback) {
    return request._postBody('/admin/setting/basic/depotReservoir/index', data, errorback);
  },
  depotReservoirAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depotReservoir/add', data, errorback);
  },
  depotReservoirEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/depotReservoir/edit', data, errorback);
  },
  depotReservoirDel(data, errorback) {
    return request._post('/admin/setting/basic/depotReservoir/delete', data, errorback);
  },
  /*仓库货架*/
  depotShelfList(data, errorback) {
    return request._postBody('/admin/setting/basic/depotShelf/index', data, errorback);
  },
  depotShelfAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depotShelf/add', data, errorback);
  },
  depotShelfEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/depotShelf/edit', data, errorback);
  },
  depotShelfDel(data, errorback) {
    return request._post('/admin/setting/basic/depotShelf/delete', data, errorback);
  },
  /*仓库货位*/
  depotStorageList(data, errorback) {
    return request._postBody('/admin/setting/basic/depotStorage/index', data, errorback);
  },
  depotStorageAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depotStorage/add', data, errorback);
  },
  depotStorageBatchAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/depotStorage/batchAdd', data, errorback);
  },
  depotStorageEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/depotStorage/edit', data, errorback);
  },
  depotStorageDel(data, errorback) {
    return request._post('/admin/setting/basic/depotStorage/delete', data, errorback);
  },
  depotStorageToSet(data, errorback) {
    return request._get('/admin/setting/basic/depotStorage/settingStock', data, errorback);
  },
  depotStorageSet(data, errorback) {
    return request._postBody('/admin/setting/basic/depotStorage/settingStock', data, errorback);
  },
  /*配送区域 */
  deliverAreaList(data, errorback) {
    return request._postBody('/admin/setting/basic/deliverArea/index', data, errorback);
  },
  deliverAreaAdd(data, errorback) {
    return request._postBody('/admin/setting/basic/deliverArea/add', data, errorback);
  },
  deliverAreaEdit(data, errorback) {
    return request._postBody('/admin/setting/basic/deliverArea/edit', data, errorback);
  },
  deliverAreaDel(data, errorback) {
    return request._post('/admin/setting/basic/deliverArea/delete', data, errorback);
  },
   deliverAreaToAdd(data, errorback) {
    return request._get('/admin/setting/basic/deliverArea/add', data, errorback);
  },
   deliverAreaToEdit(data, errorback) {
    return request._get('/admin/setting/basic/deliverArea/edit', data, errorback);
  },
}

export default SettingApi;
