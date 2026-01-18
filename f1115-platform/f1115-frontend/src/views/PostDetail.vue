<template>
  <div class="post-detail-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <div class="header-content">
          <el-button @click="router.back()" icon="ArrowLeft">返回</el-button>
          <h1>帖子详情</h1>
          <div></div>
        </div>
      </el-header>
      
      <!-- 主内容区 -->
      <el-main v-if="post">
        <el-card class="post-card">
          <!-- 帖子头部 -->
          <div class="post-header">
            <el-avatar 
              :src="getImageUrl(post.user?.avatar)" 
              :size="50"
              class="clickable-avatar"
              @click="goToUserProfile(post.userId)"
            >
              {{ post.user?.nickname?.[0] || post.user?.username?.[0] }}
            </el-avatar>
            <div class="user-info">
              <div class="nickname clickable-name" @click="goToUserProfile(post.userId)">
                {{ post.user?.nickname || post.user?.username }}
              </div>
              <div class="time">{{ formatTime(post.createTime) }}</div>
            </div>
          </div>
          
          <!-- 帖子内容 -->
          <div class="post-content">
            {{ post.content }}
          </div>
          
          <!-- 帖子图片 -->
          <div v-if="post.imageList && post.imageList.length > 0" class="post-images">
            <el-image
              v-for="(img, index) in post.imageList"
              :key="index"
              :src="getImageUrl(img)"
              :preview-src-list="post.imageList.map(i => getImageUrl(i))"
              :initial-index="index"
              fit="cover"
              class="post-image"
            />
          </div>
          
          <!-- 帖子统计 -->
          <div class="post-stats">
            <el-space :size="20">
              <span>
                <el-icon><View /></el-icon>
                {{ post.viewCount }} 浏览
              </span>
              <span>
                <el-icon><Star /></el-icon>
                {{ post.likeCount }} 点赞
              </span>
              <span>
                <el-icon><ChatDotRound /></el-icon>
                {{ post.commentCount }} 评论
              </span>
            </el-space>
          </div>
          
          <!-- 操作按钮 -->
          <div class="post-actions">
            <el-button 
              :type="post.isLiked ? 'primary' : 'default'"
              @click="handleTogglePostLike"
              :loading="likingPost"
            >
              <el-icon><component :is="post.isLiked ? 'StarFilled' : 'Star'" /></el-icon>
              {{ post.isLiked ? '已点赞' : '点赞' }}
            </el-button>
            <el-button icon="ChatDotRound" @click="scrollToCommentInput">评论</el-button>
          </div>
        </el-card>
        
        <!-- 评论区域 -->
        <el-card class="comment-section">
          <template #header>
            <div class="section-header">
              <span>评论 ({{ post.commentCount }})</span>
            </div>
          </template>
          
          <!-- 评论输入框 -->
          <div class="comment-input">
            <el-input
              v-model="commentContent"
              type="textarea"
              :rows="3"
              placeholder="发表你的评论..."
              maxlength="500"
              show-word-limit
            />
            <el-button 
              type="primary" 
              @click="handleSubmitComment" 
              :loading="commenting"
              style="margin-top: 10px"
            >
              发布评论
            </el-button>
          </div>
          
          <!-- 评论列表 -->
          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <!-- 评论者信息 -->
              <div class="comment-header">
                <el-avatar 
                  :src="getImageUrl(comment.user?.avatar)" 
                  :size="36"
                  class="clickable-avatar"
                  @click="goToUserProfile(comment.userId)"
                >
                  {{ comment.user?.nickname?.[0] || comment.user?.username?.[0] }}
                </el-avatar>
                <div class="comment-info">
                  <div class="comment-user clickable-name" @click="goToUserProfile(comment.userId)">
                    {{ comment.user?.nickname || comment.user?.username }}
                  </div>
                  <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
                </div>
              </div>
              
              <!-- 评论内容 -->
              <div class="comment-content">{{ comment.content }}</div>
              
              <!-- 评论操作 -->
              <div class="comment-actions">
                <el-button text size="small" @click="handleReply(comment)">
                  <el-icon><ChatDotRound /></el-icon>
                  回复
                </el-button>
                <el-button 
                  text 
                  size="small"
                  :type="comment.isLiked ? 'primary' : 'default'"
                  @click="handleToggleCommentLike(comment)"
                >
                  <el-icon><component :is="comment.isLiked ? 'StarFilled' : 'Star'" /></el-icon>
                  {{ comment.likeCount }}
                </el-button>
                <el-button 
                  v-if="userStore.currentUser && comment.userId === userStore.currentUser.id"
                  text 
                  size="small" 
                  type="danger"
                  @click="handleDeleteComment(comment.id)"
                >
                  删除
                </el-button>
              </div>
              
              <!-- 回复列表 -->
              <div v-if="comment.replies && comment.replies.length > 0" class="replies">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <el-avatar 
                    :src="getImageUrl(reply.user?.avatar)" 
                    :size="30"
                    class="clickable-avatar"
                    @click="goToUserProfile(reply.userId)"
                  >
                    {{ reply.user?.nickname?.[0] || reply.user?.username?.[0] }}
                  </el-avatar>
                  <div class="reply-content">
                    <div>
                      <span class="reply-user clickable-name" @click="goToUserProfile(reply.userId)">
                        {{ reply.user?.nickname || reply.user?.username }}
                      </span>
                      <span v-if="reply.parentComment" class="reply-to">
                        回复 
                        <span class="clickable-name" @click="goToUserProfile(reply.parentComment.userId)">
                          {{ reply.parentComment.user?.nickname || reply.parentComment.user?.username }}
                        </span>
                      </span>
                      : {{ reply.content }}
                    </div>
                    <div class="reply-footer">
                      <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                      <el-button text size="small" @click="handleReply(reply)">回复</el-button>
                      <el-button 
                        text 
                        size="small"
                        :type="reply.isLiked ? 'primary' : 'default'"
                        @click.stop="handleToggleCommentLike(reply)"
                      >
                        <el-icon><component :is="reply.isLiked ? 'StarFilled' : 'Star'" /></el-icon>
                        {{ reply.likeCount }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 回复输入框 -->
              <div v-if="replyingTo && replyingTo.id === comment.id" class="reply-input">
                <el-input
                  v-model="replyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${comment.user?.nickname || comment.user?.username}...`"
                  maxlength="500"
                />
                <div style="margin-top: 10px">
                  <el-button size="small" @click="replyingTo = null">取消</el-button>
                  <el-button 
                    type="primary" 
                    size="small" 
                    @click="handleSubmitReply" 
                    :loading="replying"
                  >
                    发布
                  </el-button>
                </div>
              </div>
            </div>
            
            <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发吧！" />
          </div>
        </el-card>
      </el-main>
      
      <el-main v-else>
        <el-skeleton :rows="10" animated />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getPostDetail } from '@/api/post'
import { getComments, createComment, deleteComment } from '@/api/comment'
import { togglePostLike, toggleCommentLike } from '@/api/like'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const post = ref(null)
const comments = ref([])
const commentContent = ref('')
const commenting = ref(false)
const replyingTo = ref(null)
const replyContent = ref('')
const replying = ref(false)
const likingPost = ref(false)

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 2592000000) return Math.floor(diff / 86400000) + '天前'
  
  return date.toLocaleDateString()
}

// 获取图片完整URL
const getImageUrl = (path) => {
  if (!path) return ''
  // 如果是完整URL，直接返回
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path
  }
  // 如果是相对路径，添加后端服务器地址
  if (path.startsWith('/')) {
    return 'http://localhost:8080' + path
  }
  return 'http://localhost:8080/' + path
}

// 跳转到用户主页
const goToUserProfile = (userId) => {
  router.push(`/user/${userId}`)
}

// 加载帖子详情
const loadPostDetail = async () => {
  try {
    const postId = route.params.postId
    const res = await getPostDetail(postId)
    if (res.code === 200) {
      post.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载帖子失败')
    router.back()
  }
}

// 加载评论列表
const loadComments = async () => {
  try {
    const postId = route.params.postId
    const res = await getComments(postId)
    if (res.code === 200) {
      comments.value = res.data
    }
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

// 发布评论
const handleSubmitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  commenting.value = true
  try {
    const postId = route.params.postId
    const res = await createComment(postId, commentContent.value)
    if (res.code === 200) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      // 重新加载评论和帖子（更新评论数）
      await loadComments()
      await loadPostDetail()
    }
  } finally {
    commenting.value = false
  }
}

// 回复评论
const handleReply = (comment) => {
  replyingTo.value = comment
  replyContent.value = ''
}

// 提交回复
const handleSubmitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  replying.value = true
  try {
    const postId = route.params.postId
    const res = await createComment(postId, replyContent.value, replyingTo.value.id)
    if (res.code === 200) {
      ElMessage.success('回复成功')
      replyContent.value = ''
      replyingTo.value = null
      // 重新加载评论
      await loadComments()
      await loadPostDetail()
    }
  } finally {
    replying.value = false
  }
}

// 删除评论
const handleDeleteComment = async (commentId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteComment(commentId)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 重新加载评论
      await loadComments()
      await loadPostDetail()
    }
  } catch (error) {
    // 用户取消
  }
}

// 点赞/取消点赞帖子
const handleTogglePostLike = async () => {
  if (!userStore.currentUser) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  likingPost.value = true
  try {
    const res = await togglePostLike(post.value.id)
    if (res.code === 200) {
      // 切换点赞状态
      post.value.isLiked = !post.value.isLiked
      post.value.likeCount += post.value.isLiked ? 1 : -1
    }
  } finally {
    likingPost.value = false
  }
}

// 点赞/取消点赞评论
const handleToggleCommentLike = async (comment) => {
  if (!userStore.currentUser) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  try {
    const res = await toggleCommentLike(comment.id)
    if (res.code === 200) {
      // 切换点赞状态
      comment.isLiked = !comment.isLiked
      comment.likeCount += comment.isLiked ? 1 : -1
    }
  } catch (error) {
    console.error('点赞失败', error)
  }
}

// 滚动到评论输入框
const scrollToCommentInput = () => {
  const commentInput = document.querySelector('.comment-input')
  if (commentInput) {
    commentInput.scrollIntoView({ behavior: 'smooth' })
  }
}

onMounted(() => {
  loadPostDetail()
  loadComments()
})
</script>

<style scoped>
.post-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-content h1 {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.el-main {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.post-card {
  margin-bottom: 20px;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-info {
  margin-left: 15px;
}

.nickname {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.clickable-avatar {
  cursor: pointer;
  transition: transform 0.2s;
}

.clickable-avatar:hover {
  transform: scale(1.1);
}

.clickable-name {
  cursor: pointer;
  transition: color 0.2s;
}

.clickable-name:hover {
  color: #409eff;
}

.time {
  font-size: 13px;
  color: #909399;
  margin-top: 5px;
}

.post-content {
  margin: 20px 0;
  line-height: 1.8;
  font-size: 15px;
  color: #303133;
  white-space: pre-wrap;
  word-break: break-word;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin: 20px 0;
}

.post-image {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  cursor: pointer;
}

.post-stats {
  padding: 15px 0;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
  color: #909399;
  font-size: 14px;
}

.post-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.comment-section {
  margin-top: 20px;
}

.section-header {
  font-weight: bold;
  font-size: 16px;
}

.comment-input {
  margin-bottom: 30px;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-info {
  margin-left: 10px;
}

.comment-user {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}

.comment-time {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.comment-content {
  margin: 10px 0 10px 46px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-actions {
  margin-left: 46px;
  display: flex;
  gap: 10px;
}

.replies {
  margin: 15px 0 0 46px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.reply-item {
  display: flex;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #e4e7ed;
}

.reply-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.reply-content {
  flex: 1;
  font-size: 14px;
  line-height: 1.6;
}

.reply-user {
  font-weight: bold;
  color: #303133;
}

.reply-to {
  color: #909399;
  margin: 0 5px;
}

.reply-footer {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 15px;
}

.reply-time {
  color: #909399;
}

.reply-input {
  margin: 15px 0 0 46px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}
</style>
