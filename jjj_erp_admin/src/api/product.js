import request from '@/utils/request'

let ProductApi = {

  /*商品列表*/
  productList(data, errorback) {
    return request._postBody('/admin/product/base/product/index', data, errorback);
  },
   productExtendIndex(data, errorback) {
    return request._postBody('/admin/product/base/product/extendIndex', data, errorback);
  },
  productAdd(data, errorback) {
    return request._postBody('/admin/product/base/product/add', data, errorback);
  },
  productToAdd(data, errorback) {
    return request._get('/admin/product/base/product/add', data, errorback);
  },
  productGetMaxProductCode(data, errorback) {
    return request._get('/admin/product/base/product/getMaxProductCode', data, errorback);
  },
  productEdit(data, errorback) {
    return request._postBody('/admin/product/base/product/edit', data, errorback);
  },
  productDelete(data, errorback) {
    return request._post('/admin/product/base/product/delete', data, errorback);
  },
  productSetStatus(data, errorback) {
    return request._post('/admin/product/base/product/state', data, errorback);
  },
  /*分类列表*/
  categoryList(data, errorback) {
    return request._postBody('/admin/product/base/category/index', data, errorback);
  },
  categoryAdd(data, errorback) {
    return request._postBody('/admin/product/base/category/add', data, errorback);
  },
  categoryEdit(data, errorback) {
    return request._postBody('/admin/product/base/category/edit', data, errorback);
  },
  categoryDelete(data, errorback) {
    return request._post('/admin/product/base/category/delete', data, errorback);
  },
  /*品牌列表*/
  mfrsList(data, errorback) {
    return request._postBody('/admin/product/base/mfrs/index', data, errorback);
  },
  mfrsAdd(data, errorback) {
    return request._postBody('/admin/product/base/mfrs/add', data, errorback);
  },
  mfrsEdit(data, errorback) {
    return request._postBody('/admin/product/base/mfrs/edit', data, errorback);
  },
  mfrsDelete(data, errorback) {
    return request._post('/admin/product/base/mfrs/delete', data, errorback);
  },


  /*单位列表*/
  unitList(data, errorback) {
    return request._postBody('/admin/product/base/unit/index', data, errorback);
  },
  unitAdd(data, errorback) {
    return request._postBody('/admin/product/base/unit/add', data, errorback);
  },
  unitEdit(data, errorback) {
    return request._postBody('/admin/product/base/unit/edit', data, errorback);
  },
  unitDelete(data, errorback) {
    return request._post('/admin/product/base/unit/delete', data, errorback);
  },
  unitSetStatus(data, errorback) {
    return request._post('/admin/product/base/unit/setStatus', data, errorback);
  },

  /*规格列表*/
  attributeList(data, errorback) {
    return request._postBody('/admin/product/base/attribute/index', data, errorback);
  },
  /*规格添加*/
  attributeAdd(data, errorback) {
    return request._postBody('/admin/product/base/attribute/add', data, errorback);
  },
  /*规格编辑*/
  attributeEdit(data, errorback) {
    return request._postBody('/admin/product/base/attribute/edit', data, errorback);
  },
  /*规格删除*/
  attributeDelete(data, errorback) {
    return request._post('/admin/product/base/attribute/delete', data, errorback);
  },
}

export default ProductApi;
