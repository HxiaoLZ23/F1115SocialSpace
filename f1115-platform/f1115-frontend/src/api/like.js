import request from './request'

/**
 * 点赞/取消点赞帖子
 */
export function togglePostLike(postId) {
  return request({
    url: `/post/${postId}/like`,
    method: 'post'
  })
}

/**
 * 点赞/取消点赞评论
 */
export function toggleCommentLike(commentId) {
  return request({
    url: `/comment/${commentId}/like`,
    method: 'post'
  })
}
