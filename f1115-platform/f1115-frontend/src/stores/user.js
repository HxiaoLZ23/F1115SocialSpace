import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, logout, getProfile } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const currentUser = ref(null)
  
  const isLoggedIn = computed(() => currentUser.value !== null)
  
  // 登录
  async function userLogin(username, password) {
    const res = await login(username, password)
    if (res.code === 200) {
      currentUser.value = res.data
      return true
    }
    return false
  }
  
  // 注册
  async function userRegister(username, password, email, nickname) {
    const res = await register(username, password, email, nickname)
    return res.code === 200
  }
  
  // 登出
  async function userLogout() {
    await logout()
    currentUser.value = null
  }
  
  // 获取用户信息
  async function fetchUserProfile() {
    const res = await getProfile()
    if (res.code === 200) {
      currentUser.value = res.data
    }
  }
  
  return {
    currentUser,
    isLoggedIn,
    userLogin,
    userRegister,
    userLogout,
    fetchUserProfile
  }
})
