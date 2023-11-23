// 引入axios
import axios from 'axios';
import store from '@/store'
import {ElMessage} from "element-plus";

let baseUrl="http://localhost:80/";
// 创建axios实例
const httpService = axios.create({
    // url前缀-'http:xxx.xxx'
    // baseURL: process.env.BASE_API,
    baseURL:baseUrl,
    // 请求超时时间
    timeout: 3000
});




//添加请求和响应拦截器
// 添加请求拦截器
httpService.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    //config.headers.token=window.sessionStorage.getItem('token');
    //httpService.defaults.headers.common['Authorization'] = store.getters.GET_TOKEN;
    // let token = window.sessionStorage.getItem("token")
    // if(token)
    // {
    //     config.headers.token = token;
    //     return config;
    // }
   // console.log("store="+store.getters.GET_TOKEN)
    config.headers.token=sessionStorage.getItem("token")

    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
httpService.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});

/*网络请求部分*/

/*
 *  get请求
 *  url:请求地址
 *  params:参数
 * */
export function get(url, params = {}) {
    return new Promise((resolve, reject) => {
        httpService({
            url: url,
            method: 'get',
            params: params
        }).then(response => {
            resolve(response);
        }).catch(error => {
            reject(error);
        });
    });
}

/*
 *  post请求
 *  url:请求地址
 *  params:参数
 * */
export function post(url, params = {}) {
    return new Promise((resolve, reject) => {
        httpService({
            url: url,
            method: 'post',
            data: params,
        }).then(response => {
            console.log(response)
            resolve(response);
        }).catch(error => {
            console.log(error)
            reject(error);
        });
    });
}

/*
 *  文件上传
 *  url:请求地址
 *  params:参数
 * */
export function fileUpload(url, params = {}) {
    return new Promise((resolve, reject) => {
        httpService({
            url: url,
            method: 'post',
            data: params,
            headers: { 'Content-Type': 'multipart/form-data' }
        }).then(response => {
            resolve(response);
        }).catch(error => {
            reject(error);
        });
    });
}


/**
 *
 * @param url
 * @param hid
 * @param filename
 * @returns {Promise<unknown>}
 * @constructor
 */
export function Download(url, hid,filename) {
    return new Promise((resolve, reject) => {
        httpService({
            url: url+''+hid,
            method: 'get',
            responseType: 'blob',
            headers:{
                'Content-Type': 'application/json; charset=UTF-8',
            },
        }).then(response => {
            console.log(response);
            if (!response || !response.data) {
                ElMessage({
                    type: 'error',
                    message: '失败!'
                })
                return;
            }
            let blob = new Blob([response.data]);//response.data为后端传的流文件
            //console.log(filename+'');
            let downloadFilename = filename+'';//设置导出的文件名
            //console.log(downloadFilename);
            //谷歌,火狐等浏览器
            let objectURL = window.URL.createObjectURL(blob);
            let downloadElement = document.createElement("a");
            downloadElement.style.display = "none";
            downloadElement.href = objectURL;
            downloadElement.download = downloadFilename;
            document.body.appendChild(downloadElement);
            downloadElement.click();
            document.body.removeChild(downloadElement);
            window.URL.revokeObjectURL(url);

            ElMessage({
                type: 'success',
                message: '下载成功!'
            })
        }).catch(err => {
            ElMessage({
                type: 'error',
                message: '失败!'
            })
        })
    });
}

export function Download2(url,hid, Sno, filename) {
    return new Promise((resolve, reject) => {
        httpService({
            url: url+''+hid+'/'+Sno,
            method: 'get',
            responseType: 'blob',
            headers:{
                'Content-Type': 'application/json; charset=UTF-8',
            },
        }).then(response => {
            console.log(response);
            if (!response || !response.data) {
                ElMessage({
                    type: 'error',
                    message: '失败!'
                })
                return;
            }
            let blob = new Blob([response.data]);//response.data为后端传的流文件
            //console.log(filename+'');
            let downloadFilename = filename+'';//设置导出的文件名
            //console.log(downloadFilename);
            //谷歌,火狐等浏览器
            let objectURL = window.URL.createObjectURL(blob);
            let downloadElement = document.createElement("a");
            downloadElement.style.display = "none";
            downloadElement.href = objectURL;
            downloadElement.download = downloadFilename;
            document.body.appendChild(downloadElement);
            downloadElement.click();
            document.body.removeChild(downloadElement);
            window.URL.revokeObjectURL(url);

            ElMessage({
                type: 'success',
                message: '下载成功!'
            })
        }).catch(err => {
            ElMessage({
                type: 'error',
                message: '失败!'
            })
        })
    });
}




export function getServerUrl(){
    return baseUrl;
}

export default {
    get,
    post,
    fileUpload,
    //fileDownload,
    Download2,
    Download,
    getServerUrl
}
