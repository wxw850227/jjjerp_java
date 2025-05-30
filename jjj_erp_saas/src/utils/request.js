import axios from 'axios';
import qs from 'qs';
import router from '@/router';
import configObj from "@/config";
import { useUserStore } from "@/store";
let { baseURL, tokenName, contentType, withCredentials, responseType } = configObj;
// const { token } = userStore;
//axios.defaults.timeout = 5000;
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'; //配置请求头
// config.headers[tokenName] = token;
axios.defaults.baseURL = baseURL;
axios.defaults.withCredentials = withCredentials;
axios.defaults.responseType = responseType;

//POST传参序列化(添加请求拦截器)
axios.interceptors.request.use((config) => {
  //在发送请求之前做某件事
  const userStore = useUserStore();
  const { token, userInfo } = userStore;
  config.headers[tokenName] = token;
  config.headers["AppID"] = userInfo && userInfo.AppID;
  if (config.method === 'post' && !config.headers.uploadImg && !config.headers.postBody) {
    config.data = qs.stringify(config.data);
  }
  return config;
}, (error) => {
  console.log('错误的传参')
  return Promise.reject(error);
});

//返回状态判断(添加响应拦截器)
axios.interceptors.response.use((res) => {
  //未登陆
  if (res.data.code !== 1) {
    console.log('未登录状态')
    if (res.data.code === 0) {
      ElMessage({
        showClose: true,
        message: res.data.msg,
        type: "error",
      });
      return Promise.reject(res.data);
    } else {
      const userStore = useUserStore();
      const { afterLogout } = userStore;
      afterLogout();
      router.push({
        path: '/login',
      })
    }
  } else {
    return res.data;
  }
}, (error) => {
  ElMessage({
    showClose: true,
    message: '接口请求异常，请稍后再试~',
    type: "error"
  });
  return Promise.reject(error);
});

/**
 * 返回一个Promise(发送post请求)
 * errorback是否错误回调
 */
export function _post(url, params, errorback) {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(response => {
        resolve(response);
      })
      .catch((error) => {
        errorback && reject(error);
      })
  })
}
/**
 * 返回一个Promise(发送post请求)
 * errorback是否错误回调
 */
export function _postBody(url, params, errorback) {
  return new Promise((resolve, reject) => {
    let headers = {
      "Content-Type": 'application/json;charset=UTF-8',
      postBody: true
    }
    axios.post(url, params, { headers })
      .then(response => {
        resolve(response);
      })
      .catch((error) => {
        errorback && reject(error);
      })
  })
}
/**
 * 返回一个Promise(发送上传请求)
 * errorback是否错误回调
 */
export function _upload(url, formData, errorback) {
  return new Promise((resolve, reject) => {
    let headers = {
      "Content-Type": "multipart/form-data",
      "uploadImg": true,
    }
    axios.post(url, formData, { headers })
      .then(response => {
        resolve(response);
      })
      .catch((error) => {
        reject(error);
      })
  })
}
/**
 * 返回一个Promise(发送get请求)
 * errorback是否错误回调
 */
export function _get(url, param, errorback) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: param
    })
      .then(response => {
        resolve(response)
      })
      .catch((error) => {
        errorback && reject(error);
      })
  })
}
export default {
  _post,
  _get,
  _upload,
  _postBody
}
