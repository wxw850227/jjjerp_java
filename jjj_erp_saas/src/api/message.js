import request from '@/utils/request'

let MessageApi = {
  /*消息列表*/
  messageList(data, errorback) {
    return request._post('/saas/message/index', data, errorback);
  },
  /*添加消息*/
  addMessage(data, errorback) {
    return request._postBody('/saas/message/add', data, errorback);
  },
  /*修改消息*/
  editMessage(data, errorback) {
    return request._postBody('/saas/message/edit', data, errorback);
  },
  /*删除消息*/
  deleteMessage(data, errorback) {
    return request._post('/saas/message/delete', data, errorback);
  },
  /*消息字段列表*/
  fieldList(data, errorback) {
    return request._post('/saas/message/field', data, errorback);
  },
  /*保存消息字段*/
  saveField(data, errorback) {
    return request._postBody('/saas/message/saveField', data, errorback);
  },
}

export default MessageApi;
