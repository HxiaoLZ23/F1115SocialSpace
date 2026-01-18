import request from './request'

/**
 * 发布评论
 */
export function createComment(postId, content, parentId = null) {
  return request({
    url: `/post/${postId}/comment`,
    method: 'post',
    data: { content, parentId }
  })
}

/**
 * 获取帖子的评论列表
 */
export function getComments(postId) {
  return request({
    url: `/post/${postId}/comments`,
    method: 'get'
  })
}

/**
 * 删除评论
 */
export function deleteComment(commentId) {
  return request({
    url: `/comment/${commentId}`,
    method: 'delete'
  })
}
