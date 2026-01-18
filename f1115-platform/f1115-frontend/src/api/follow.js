import request from './request'

/**
 * 关注/取消关注用户
 */
export function toggleFollow(userId) {
  return request({
    url: `/user/${userId}/follow`,
    method: 'post'
  })
}

/**
 * 获取关注列表
 */
export function getFollowing(userId, pageNum = 1, pageSize = 20) {
  return request({
    url: `/user/${userId}/following`,
    method: 'get',
    params: { pageNum, pageSize }
  })
}

/**
 * 获取粉丝列表
 */
export function getFollowers(userId, pageNum = 1, pageSize = 20) {
  return request({
    url: `/user/${userId}/followers`,
    method: 'get',
    params: { pageNum, pageSize }
  })
}
