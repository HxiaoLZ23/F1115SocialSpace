import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true // 携带cookie
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据响应码处理
    if (res.code === 200) {
      return res
    } else if (res.code === 401) {
      // 未登录
      ElMessage.error(res.msg || '未登录或登录已过期')
      router.push('/login')
      return Promise.reject(new Error(res.msg || '未登录'))
    } else {
      // 其他错误
      ElMessage.error(res.msg || '操作失败')
      return Promise.reject(new Error(res.msg || '操作失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    ElMessage.error('网络错误，请稍后重试')
    return Promise.reject(error)
  }
)

export default request
