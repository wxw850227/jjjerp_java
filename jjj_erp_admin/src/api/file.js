import request from '@/utils/request'

let FileApi = {
    /*文件类别列表*/
    SystemPictureList(data, errorback) {
        return request._postBody('/admin/image/bank/list', data, errorback);
    },

    /*系统列表*/
    PictureIndex(data, errorback) {
        return request._postBody('/admin/image/bank/index', data, errorback);
    },
    /*文件类别列表*/
    fileCategory(data, errorback) {
        return request._post('/admin/file/file/category', data, errorback);
    },

    /*文件列表*/
    fileList(data, errorback) {
        return request._postBody('/admin/file/file/lists', data, errorback);
    },

    /*删除多文件*/
    deleteFiles(data, errorback) {
        return request._post('/admin/file/file/deleteFiles', data, errorback);
    },

    /*添加文件分类*/
    addCategory(data, errorback) {
        return request._post('/admin/file/file/addGroup', data, errorback);
    },

    /*修改文件分类*/
    editCategory(data, errorback) {
        return request._post('/admin/file/file/editGroup', data, errorback);
    },

    /*删除文件分类*/
    deleteCategory(data, errorback) {
        return request._post('/admin/file/file/deleteGroup', data, errorback);
    },

    /*上传*/
    uploadFile(formData, errorback) {
        return request._upload('/admin/file/upload/image', formData, errorback);
    },
    /*移动文件*/
    moveFile(formData, errorback) {
        return request._post('/admin/file/file/moveFiles', formData, errorback);
    },


}

export default FileApi;
