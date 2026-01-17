<template>
  <div class="profile-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <div class="header-content">
          <h1>个人中心</h1>
          <el-button @click="router.push('/home')">返回首页</el-button>
        </div>
      </el-header>
      
      <!-- 主内容区 -->
      <el-main>
        <el-card>
          <el-descriptions title="用户信息" :column="2" border>
            <el-descriptions-item label="用户名">
              {{ userStore.currentUser?.username }}
            </el-descriptions-item>
            <el-descriptions-item label="昵称">
              {{ userStore.currentUser?.nickname }}
            </el-descriptions-item>
            <el-descriptions-item label="邮箱">
              {{ userStore.currentUser?.email || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="个人简介">
              {{ userStore.currentUser?.bio || '暂无简介' }}
            </el-descriptions-item>
            <el-descriptions-item label="注册时间">
              {{ userStore.currentUser?.createTime }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  // 刷新用户信息
  await userStore.fetchUserProfile()
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
  font-size: 24px;
  color: #303133;
}

.el-main {
  padding: 20px;
}
</style>
