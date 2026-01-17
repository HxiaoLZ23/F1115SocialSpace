import request from './request'

/**
 * 用户登录
 */
export function login(username, password) {
  return request({
    url: '/user/login',
    method: 'post',
    data: { username, password }
  })
}

/**
 * 用户注册
 */
export function register(username, password, email, nickname) {
  return request({
    url: '/user/register',
    method: 'post',
    data: { username, password, email, nickname }
  })
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

/**
 * 获取当前用户信息
 */
export function getProfile() {
  return request({
    url: '/user/profile',
    method: 'get'
  })
}

/**
 * 更新用户信息
 */
export function updateProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data
  })
}

/**
 * 修改密码
 */
export function updatePassword(oldPassword, newPassword) {
  return request({
    url: '/user/password',
    method: 'put',
    data: { oldPassword, newPassword }
  })
}

/**
 * 获取指定用户信息
 */
export function getUserInfo(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'get'
  })
}
