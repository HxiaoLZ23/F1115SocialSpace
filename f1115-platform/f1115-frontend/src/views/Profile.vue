<template>
  <div class="profile-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <div class="header-content">
          <el-button @click="router.push('/home')" icon="ArrowLeft">返回首页</el-button>
          <h1>个人主页</h1>
          <div></div>
        </div>
      </el-header>
      
      <!-- 主内容区 -->
      <el-main v-if="userInfo">
        <!-- 用户信息卡片 -->
        <el-card class="user-info-card">
          <div class="user-header">
            <!-- 头像 -->
            <div class="avatar-section">
              <el-avatar :src="getImageUrl(userInfo.avatar)" :size="100">
                {{ userInfo.nickname?.[0] || userInfo.username?.[0] }}
              </el-avatar>
              <el-upload
                v-if="isOwnProfile"
                :auto-upload="false"
                :on-change="handleAvatarChange"
                :show-file-list="false"
                accept="image/*"
                class="avatar-upload"
              >
                <el-button size="small" icon="Camera">更换头像</el-button>
              </el-upload>
            </div>
            
            <!-- 用户信息 -->
            <div class="user-details">
              <h2>{{ userInfo.nickname || userInfo.username }}</h2>
              <p class="username">@{{ userInfo.username }}</p>
              <p class="bio">{{ userInfo.bio || '这个人很懒，什么都没写...' }}</p>
              
              <!-- 统计数据 -->
              <div class="stats">
                <div class="stat-item">
                  <span class="stat-value">{{ userInfo.postCount || 0 }}</span>
                  <span class="stat-label">帖子</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ userInfo.followingCount || 0 }}</span>
                  <span class="stat-label">关注</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ userInfo.followerCount || 0 }}</span>
                  <span class="stat-label">粉丝</span>
                </div>
              </div>
              
              <!-- 操作按钮 -->
              <div class="actions">
                <el-button v-if="isOwnProfile" type="primary" @click="showEditDialog = true">
                  编辑资料
                </el-button>
                <el-button 
                  v-else 
                  :type="userInfo.isFollowed ? 'default' : 'primary'"
                  @click="handleToggleFollow"
                  :loading="following"
                >
                  {{ userInfo.isFollowed ? '已关注' : '关注' }}
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
        
        <!-- 标签页 -->
        <el-card class="tabs-card">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="我的帖子" name="posts">
              <!-- 帖子列表 -->
              <div class="post-list">
                <el-card
                  v-for="post in posts"
                  :key="post.id"
                  class="post-item"
                  @click="goToPostDetail(post.id)"
                >
                  <div class="post-content">{{ post.content }}</div>
                  
                  <!-- 图片展示 -->
                  <div v-if="post.imageList && post.imageList.length > 0" class="post-images">
                    <el-image
                      v-for="(img, index) in post.imageList.slice(0, 9)"
                      :key="index"
                      :src="getImageUrl(img)"
                      fit="cover"
                      class="post-image"
                      @click.stop
                    />
                  </div>
                  
                  <div class="post-footer">
                    <span class="time">{{ formatTime(post.createTime) }}</span>
                    <el-space>
                      <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount }}</span>
                      <span><el-icon><Star /></el-icon> {{ post.likeCount }}</span>
                      <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                    </el-space>
                  </div>
                </el-card>
                
                <el-empty v-if="posts.length === 0" description="还没有发布帖子" />
                
                <!-- 分页 -->
                <div v-if="total > pageSize" class="pagination">
                  <el-pagination
                    v-model:current-page="currentPage"
                    v-model:page-size="pageSize"
                    :total="total"
                    layout="prev, pager, next"
                    @current-change="loadUserPosts"
                  />
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="关注" name="following">
              <div class="user-list">
                <el-card
                  v-for="user in followingList"
                  :key="user.id"
                  class="user-card-item"
                  @click="goToUserProfile(user.id)"
                >
                  <div class="user-item">
                    <el-avatar :src="getImageUrl(user.avatar)" :size="50">
                      {{ user.nickname?.[0] || user.username?.[0] }}
                    </el-avatar>
                    <div class="user-item-info">
                      <div class="user-name">{{ user.nickname || user.username }}</div>
                      <div class="user-bio">{{ user.bio || '暂无简介' }}</div>
                    </div>
                  </div>
                </el-card>
                <el-empty v-if="followingList.length === 0" description="还没有关注任何人" />
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="粉丝" name="followers">
              <div class="user-list">
                <el-card
                  v-for="user in followersList"
                  :key="user.id"
                  class="user-card-item"
                  @click="goToUserProfile(user.id)"
                >
                  <div class="user-item">
                    <el-avatar :src="getImageUrl(user.avatar)" :size="50">
                      {{ user.nickname?.[0] || user.username?.[0] }}
                    </el-avatar>
                    <div class="user-item-info">
                      <div class="user-name">{{ user.nickname || user.username }}</div>
                      <div class="user-bio">{{ user.bio || '暂无简介' }}</div>
                    </div>
                  </div>
                </el-card>
                <el-empty v-if="followersList.length === 0" description="还没有粉丝" />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-main>
      
      <el-main v-else>
        <el-skeleton :rows="10" animated />
      </el-main>
    </el-container>
    
    <!-- 编辑资料对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input
            v-model="editForm.bio"
            type="textarea"
            :rows="4"
            placeholder="介绍一下自己吧..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveProfile" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateProfile, uploadAvatar } from '@/api/user'
import { getUserPosts } from '@/api/post'
import { toggleFollow, getFollowing, getFollowers } from '@/api/follow'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const userInfo = ref(null)
const posts = ref([])
const activeTab = ref('posts')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showEditDialog = ref(false)
const saving = ref(false)
const following = ref(false)
const followingList = ref([])
const followersList = ref([])

const editForm = ref({
  nickname: '',
  email: '',
  bio: ''
})

// 是否是自己的主页
const isOwnProfile = computed(() => {
  return userStore.currentUser && userInfo.value && 
         userStore.currentUser.id === userInfo.value.id
})

// 获取图片完整URL
const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path
  }
  if (path.startsWith('/')) {
    return 'http://localhost:8080' + path
  }
  return 'http://localhost:8080/' + path
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
}

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const userId = route.params.userId || userStore.currentUser?.id
    if (!userId) {
      router.push('/login')
      return
    }
    
    const res = await getUserInfo(userId)
    if (res.code === 200) {
      userInfo.value = res.data
      // 初始化编辑表单
      editForm.value = {
        nickname: res.data.nickname || '',
        email: res.data.email || '',
        bio: res.data.bio || ''
      }
    }
  } catch (error) {
    ElMessage.error('加载用户信息失败')
  }
}

// 加载用户帖子
const loadUserPosts = async () => {
  try {
    const userId = route.params.userId || userStore.currentUser?.id
    const res = await getUserPosts(userId, currentPage.value, pageSize.value)
    if (res.code === 200) {
      posts.value = res.data
      total.value = res.total
    }
  } catch (error) {
    console.error('加载帖子失败', error)
  }
}

// 上传头像
const handleAvatarChange = async (file) => {
  try {
    const res = await uploadAvatar(file.raw)
    if (res.code === 200) {
      ElMessage.success('头像更新成功')
      userInfo.value.avatar = res.data
      // 更新store中的用户信息
      await userStore.fetchUserProfile()
    }
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}

// 保存个人资料
const handleSaveProfile = async () => {
  saving.value = true
  try {
    const res = await updateProfile(editForm.value)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      showEditDialog.value = false
      // 重新加载用户信息
      await loadUserInfo()
      // 更新store中的用户信息
      await userStore.fetchUserProfile()
    }
  } finally {
    saving.value = false
  }
}

// 跳转到帖子详情
const goToPostDetail = (postId) => {
  router.push(`/post/${postId}`)
}

// 跳转到用户主页
const goToUserProfile = (userId) => {
  // 使用replace而不是push，避免路由重复
  if (route.params.userId !== userId.toString()) {
    router.push(`/user/${userId}`)
    // 重新加载数据
    setTimeout(() => {
      loadUserInfo()
      loadUserPosts()
      // 重置标签页
      activeTab.value = 'posts'
      followingList.value = []
      followersList.value = []
    }, 100)
  }
}

// 关注/取消关注
const handleToggleFollow = async () => {
  if (!userStore.currentUser) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  following.value = true
  try {
    const res = await toggleFollow(userInfo.value.id)
    if (res.code === 200) {
      // 切换关注状态
      userInfo.value.isFollowed = !userInfo.value.isFollowed
      userInfo.value.followerCount += userInfo.value.isFollowed ? 1 : -1
      ElMessage.success(res.msg)
    }
  } finally {
    following.value = false
  }
}

// 加载关注列表
const loadFollowing = async () => {
  try {
    const userId = route.params.userId || userStore.currentUser?.id
    const res = await getFollowing(userId, 1, 100)
    if (res.code === 200) {
      followingList.value = res.data
    }
  } catch (error) {
    console.error('加载关注列表失败', error)
  }
}

// 加载粉丝列表
const loadFollowers = async () => {
  try {
    const userId = route.params.userId || userStore.currentUser?.id
    const res = await getFollowers(userId, 1, 100)
    if (res.code === 200) {
      followersList.value = res.data
    }
  } catch (error) {
    console.error('加载粉丝列表失败', error)
  }
}

// 监听标签页切换
watch(activeTab, (newTab) => {
  if (newTab === 'following') {
    loadFollowing()
  } else if (newTab === 'followers') {
    loadFollowers()
  }
})

// 监听路由变化，重新加载数据
watch(() => route.params.userId, (newUserId, oldUserId) => {
  if (newUserId && newUserId !== oldUserId) {
    // 重置数据
    userInfo.value = null
    posts.value = []
    followingList.value = []
    followersList.value = []
    activeTab.value = 'posts'
    currentPage.value = 1
    
    // 重新加载
    loadUserInfo()
    loadUserPosts()
  }
})

onMounted(() => {
  loadUserInfo()
  loadUserPosts()
})
</script>

<style scoped>
.profile-container {
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
  max-width: 1000px;
  margin: 0 auto;
}

.user-info-card {
  margin-bottom: 20px;
}

.user-header {
  display: flex;
  gap: 30px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.avatar-upload {
  width: 100%;
}

.user-details {
  flex: 1;
}

.user-details h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #303133;
}

.username {
  color: #909399;
  font-size: 14px;
  margin: 0 0 10px 0;
}

.bio {
  color: #606266;
  line-height: 1.6;
  margin: 10px 0 20px 0;
}

.stats {
  display: flex;
  gap: 30px;
  margin: 20px 0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.actions {
  margin-top: 20px;
}

.tabs-card {
  margin-top: 20px;
}

.post-list {
  min-height: 300px;
}

.post-item {
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.post-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.post-content {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #303133;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin: 15px 0;
}

.post-image {
  width: 100%;
  height: 120px;
  border-radius: 8px;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
  color: #909399;
  font-size: 14px;
}

.time {
  color: #909399;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.user-list {
  min-height: 300px;
}

.user-card-item {
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.user-card-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.user-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-item-info {
  flex: 1;
}

.user-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.user-bio {
  font-size: 14px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
