import request from '@/utils/request'

let FileApi = {

    /*文件类别列表*/
    SystemPictureList(data, errorback) {
        return request._postBody('/saas/image/bank/list', data, errorback);
    },

    /*系统列表*/
    PictureIndex(data, errorback) {
        return request._postBody('/saas/image/bank/index', data, errorback);
    },

    /*删除多文件*/
    deleteFiles(data, errorback) {
        return request._post('/saas/image/bank/deleteFiles', data, errorback);
    },

    /*添加文件分类*/
    addCategory(data, errorback) {
        return request._postBody('/saas/image/bank/addCategory', data, errorback);
    },

    /*修改文件分类*/
    editCategory(data, errorback) {
        return request._postBody('/saas/image/bank/edit', data, errorback);
    },

    /*删除文件分类*/
    deleteCategory(data, errorback) {
        return request._post('/saas/image/bank/delete', data, errorback);
    },
    /*上传*/
    uploadFile(formData, errorback) {
        return request._upload('/saas/file/upload/image', formData, errorback);
    },
}

export default FileApi;
