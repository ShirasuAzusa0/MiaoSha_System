<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <nav class="navbar">
          <div class="nav-container">
            <div class="nav-brand">
              <h2>⚡ 秒杀商城</h2>
            </div>
            <div class="nav-menu">
              <button @click="router.push('/')" class="nav-btn">首页</button>
              <div v-if="isLoggedIn">
                <button @click="router.push(`/user/${userStore.user?.userId}`)" class="nav-btn">
                  个人中心
                </button>
                <button @click="router.push('/cart')" class="nav-btn" style="margin-left: 1rem">
                  购物车
                </button>
                <button @click="logOut" class="nav-btn" style="margin-left: 1rem">退出登录</button>
              </div>
              <button v-else @click="router.push('/login')" class="nav-btn">登录</button>
            </div>
          </div>
        </nav>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const { isLoggedIn } = storeToRefs(userStore)

const logOut = () => {
  userStore.logout()
  router.push('/')
}
</script>

<style lang="scss">
@import '@/styles/variables.scss';

.common-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-header {
  --el-header-padding: 0;
  position: sticky;
  top: 0;
  width: 100%;
  z-index: 100;
}

.navbar {
  background: $gradient-primary;
  color: white;
  padding: 1rem 0;
  box-shadow: $shadow-lg;

  .nav-container {
    width: 100%;
    margin: 0 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 2rem;
  }

  .nav-brand h2 {
    font-size: 1.5rem;
    font-weight: bold;
  }

  .nav-menu {
    display: flex;
    align-items: center;
    gap: 1rem;

    .nav-btn {
      background: none;
      border: none;
      color: white;
      padding: 0.5rem 1rem;
      border-radius: $border-radius-sm;
      cursor: pointer;
      transition: $transition-base;

      &:hover,
      &.active {
        background: rgba(255, 255, 255, 0.2);
      }
    }

    .cart-icon {
      position: relative;
      font-size: 1.2rem;
      cursor: pointer;

      .cart-count {
        position: absolute;
        top: -8px;
        right: -8px;
        background: $color-danger;
        color: white;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 0.8rem;
      }
    }
  }
}

.main-content {
  padding: 20px;
}
</style>
