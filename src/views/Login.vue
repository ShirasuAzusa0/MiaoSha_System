<template>
  <div class="auth-page">
    <div class="auth-container">
      <!-- 认证卡片 -->
      <div class="auth-card">
        <!-- 返回按钮 -->
        <button class="back-to-home-btn" @click="router.push('/')">
          <span class="back-icon">←</span>
          返回首页
        </button>

        <!-- 标题区域 -->
        <div class="auth-header">
          <div class="logo">
            <span class="logo-icon">⚡</span>
            <h1>秒杀商城</h1>
          </div>
          <p class="auth-subtitle">
            {{ isLogin ? '欢迎回来！请登录您的账户' : '创建新账户，开始购物之旅' }}
          </p>
        </div>

        <!-- 切换标签 -->
        <div class="auth-tabs">
          <button @click="isLogin = true" :class="{ active: isLogin }" class="tab-btn">登录</button>
          <button @click="isLogin = false" :class="{ active: !isLogin }" class="tab-btn">
            注册
          </button>
        </div>

        <!-- 登录表单 -->
        <form v-if="isLogin" @submit.prevent="handleLogin" class="auth-form">
          <div class="form-group">
            <label for="login-email">邮箱地址</label>
            <div class="input-wrapper">
              <span class="input-icon">📧</span>
              <input
                id="login-email"
                v-model="loginForm.email"
                type="email"
                placeholder="请输入您的邮箱"
                :class="{ error: loginErrors.email }"
                @blur="validateLoginField('email')"
                @input="clearLoginError('email')"
              />
            </div>
            <span v-if="loginErrors.email" class="error-message">{{ loginErrors.email }}</span>
          </div>

          <div class="form-group">
            <label for="login-password">密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input
                id="login-password"
                v-model="loginForm.password"
                :type="showLoginPassword ? 'text' : 'password'"
                placeholder="请输入您的密码"
                :class="{ error: loginErrors.password }"
                @blur="validateLoginField('password')"
                @input="clearLoginError('password')"
              />
              <button
                type="button"
                @click="showLoginPassword = !showLoginPassword"
                class="password-toggle"
              >
                {{ showLoginPassword ? '🙈' : '👁️' }}
              </button>
            </div>
            <span v-if="loginErrors.password" class="error-message">{{
              loginErrors.password
            }}</span>
          </div>

          <button type="submit" class="auth-btn login-btn" :disabled="loginLoading">
            <span v-if="loginLoading" class="loading-spinner"></span>
            {{ loginLoading ? '登录中...' : '登录' }}
          </button>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="handleRegister" class="auth-form">
          <div class="form-group">
            <label for="register-username">用户名</label>
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <input
                id="register-username"
                v-model="registerForm.username"
                type="text"
                placeholder="请输入用户名"
                :class="{ error: registerErrors.username }"
                @blur="validateRegisterField('username')"
                @input="clearRegisterError('username')"
              />
            </div>
            <span v-if="registerErrors.username" class="error-message">{{
              registerErrors.username
            }}</span>
          </div>

          <div class="form-group">
            <label for="register-email">邮箱地址</label>
            <div class="input-wrapper">
              <span class="input-icon">📧</span>
              <input
                id="register-email"
                v-model="registerForm.email"
                type="email"
                placeholder="请输入您的邮箱"
                :class="{ error: registerErrors.email }"
                @blur="validateRegisterField('email')"
                @input="clearRegisterError('email')"
              />
            </div>
            <span v-if="registerErrors.email" class="error-message">{{
              registerErrors.email
            }}</span>
          </div>

          <div class="form-group">
            <label for="register-password">密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input
                id="register-password"
                v-model="registerForm.password"
                :type="showRegisterPassword ? 'text' : 'password'"
                placeholder="请输入密码（至少6位）"
                :class="{ error: registerErrors.password }"
                @blur="validateRegisterField('password')"
                @input="clearRegisterError('password')"
              />
              <button
                type="button"
                @click="showRegisterPassword = !showRegisterPassword"
                class="password-toggle"
              >
                {{ showRegisterPassword ? '🙈' : '👁️' }}
              </button>
            </div>
            <span v-if="registerErrors.password" class="error-message">{{
              registerErrors.password
            }}</span>
          </div>

          <div class="form-group">
            <label for="register-confirm-password">确认密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input
                id="register-confirm-password"
                v-model="registerForm.confirmPassword"
                :type="showConfirmPassword ? 'text' : 'password'"
                placeholder="请再次输入密码"
                :class="{ error: registerErrors.confirmPassword }"
                @blur="validateRegisterField('confirmPassword')"
                @input="clearRegisterError('confirmPassword')"
              />
              <button
                type="button"
                @click="showConfirmPassword = !showConfirmPassword"
                class="password-toggle"
              >
                {{ showConfirmPassword ? '🙈' : '👁️' }}
              </button>
            </div>
            <span v-if="registerErrors.confirmPassword" class="error-message">{{
              registerErrors.confirmPassword
            }}</span>
          </div>

          <button type="submit" class="auth-btn register-btn" :disabled="registerLoading">
            <span v-if="registerLoading" class="loading-spinner"></span>
            {{ registerLoading ? '注册中...' : '创建账户' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 状态管理
const isLogin = ref(true)
const loginLoading = ref(false)
const registerLoading = ref(false)
const showLoginPassword = ref(false)
const showRegisterPassword = ref(false)
const showConfirmPassword = ref(false)

// 登录表单
const loginForm = reactive({
  email: '',
  password: '',
})

// 注册表单
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})

// 错误状态
const loginErrors = reactive({
  email: '',
  password: '',
})

const registerErrors = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})

// 验证规则
const validateEmail = (email: string) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// 登录表单验证
const validateLoginField = (field: 'email' | 'password') => {
  switch (field) {
    case 'email':
      if (!loginForm.email) {
        loginErrors.email = '请输入邮箱地址'
      } else if (!validateEmail(loginForm.email)) {
        loginErrors.email = '请输入有效的邮箱地址'
      } else {
        loginErrors.email = ''
      }
      break
    case 'password':
      if (!loginForm.password) {
        loginErrors.password = '请输入密码'
      } else {
        loginErrors.password = ''
      }
      break
  }
}

// 注册表单验证
const validateRegisterField = (field: 'username' | 'email' | 'password' | 'confirmPassword') => {
  switch (field) {
    case 'username':
      if (!registerForm.username) {
        registerErrors.username = '请输入用户名'
      } else if (registerForm.username.length < 2) {
        registerErrors.username = '用户名至少需要2个字符'
      } else {
        registerErrors.username = ''
      }
      break
    case 'email':
      if (!registerForm.email) {
        registerErrors.email = '请输入邮箱地址'
      } else if (!validateEmail(registerForm.email)) {
        registerErrors.email = '请输入有效的邮箱地址'
      } else {
        registerErrors.email = ''
      }
      break
    case 'password':
      if (!registerForm.password) {
        registerErrors.password = '请输入密码'
      } else if (registerForm.password.length < 6) {
        registerErrors.password = '密码至少需要6位字符'
      } else {
        registerErrors.password = ''
      }
      break
    case 'confirmPassword':
      if (!registerForm.confirmPassword) {
        registerErrors.confirmPassword = '请确认密码'
      } else if (registerForm.confirmPassword !== registerForm.password) {
        registerErrors.confirmPassword = '两次输入的密码不一致'
      } else {
        registerErrors.confirmPassword = ''
      }
      break
  }
}

// 清除错误
const clearLoginError = (field: 'email' | 'password') => {
  loginErrors[field] = ''
}

const clearRegisterError = (field: 'username' | 'email' | 'password' | 'confirmPassword') => {
  registerErrors[field] = ''
}

// 登录处理
const handleLogin = async () => {
  // 验证所有字段
  validateLoginField('email')
  validateLoginField('password')

  // 检查是否有错误
  if (loginErrors.email || loginErrors.password) {
    return
  }

  loginLoading.value = true

  try {
    await userStore.login(loginForm.email, loginForm.password)
    ElMessage.success('登录成功！')
    router.push('/') // Redirect to home or dashboard after successful login

    // 重置表单
    Object.assign(loginForm, {
      email: '',
      password: '',
    })
  } catch (error) {
    ElMessage.error('登录失败，请检查您的邮箱和密码')
    console.error('Login error:', error)
  } finally {
    loginLoading.value = false
  }
}

// 注册处理
const handleRegister = async () => {
  // 验证所有字段
  const fieldsToValidate: Array<keyof typeof registerErrors> = [
    'username',
    'email',
    'password',
    'confirmPassword',
  ]
  fieldsToValidate.forEach((field) => {
    validateRegisterField(field)
  })

  // 检查是否有错误
  const hasErrors = Object.values(registerErrors).some((error) => error !== '')
  if (hasErrors) {
    return
  }

  registerLoading.value = true

  try {
    await userStore.signUp(registerForm.username, registerForm.email, registerForm.password)
    ElMessage.success('注册成功！请登录您的账户')

    // 切换到登录页面
    isLogin.value = true

    // 重置表单
    Object.assign(registerForm, {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    })
  } catch (error) {
    ElMessage.error('注册失败，请重试')
    console.error('Registration error:', error)
  } finally {
    registerLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.auth-container {
  width: 100%;
  max-width: 450px;
  position: relative;
  z-index: 1;
}

.back-to-home-btn {
  position: absolute;
  top: 1.5rem;
  left: 1.5rem;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  color: $color-primary;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: $transition-base;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  backdrop-filter: blur(10px);

  .back-icon {
    font-size: 1.1rem;
    transition: $transition-base;
  }

  &:hover {
    background: rgba(102, 126, 234, 0.15);
    border-color: rgba(102, 126, 234, 0.3);
    transform: translateX(-2px);

    .back-icon {
      transform: translateX(-2px);
    }
  }

  &:active {
    transform: translateX(-1px);
  }
}

// 调整 auth-card 的 position 为 relative
.auth-card {
  position: relative; // 添加这行
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.auth-header {
  text-align: center;
  margin-bottom: 2rem;

  .logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    margin-bottom: 1rem;

    .logo-icon {
      font-size: 2rem;
      background: $gradient-primary;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    h1 {
      font-size: 1.8rem;
      font-weight: 700;
      color: $color-text-primary;
    }
  }

  .auth-subtitle {
    color: $color-text-secondary;
    font-size: 1rem;
    line-height: 1.5;
  }
}

.auth-tabs {
  display: flex;
  background: $color-bg-light;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 2rem;

  .tab-btn {
    flex: 1;
    padding: 0.8rem 1rem;
    border: none;
    background: transparent;
    border-radius: 8px;
    font-weight: 600;
    color: $color-text-secondary;
    cursor: pointer;
    transition: $transition-base;

    &.active {
      background: white;
      color: $color-primary;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &:hover:not(.active) {
      color: $color-text-primary;
    }
  }
}

.auth-form {
  .form-group {
    margin-bottom: 1.5rem;

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: $color-text-primary;
      font-size: 0.9rem;
    }

    .input-wrapper {
      position: relative;
      display: flex;
      align-items: center;

      .input-icon {
        position: absolute;
        left: 1rem;
        font-size: 1.1rem;
        z-index: 1;
      }

      input {
        width: 100%;
        padding: 1rem 1rem 1rem 3rem;
        border: 2px solid $color-border;
        border-radius: 12px;
        font-size: 1rem;
        transition: $transition-base;
        background: white;

        &:focus {
          outline: none;
          border-color: $color-primary;
          box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        &.error {
          border-color: $color-danger;
          box-shadow: 0 0 0 3px rgba(255, 71, 87, 0.1);
        }

        &::placeholder {
          color: $color-text-muted;
        }
      }

      .password-toggle {
        position: absolute;
        right: 1rem;
        background: none;
        border: none;
        font-size: 1.1rem;
        cursor: pointer;
        padding: 0.2rem;
        border-radius: 4px;
        transition: $transition-base;

        &:hover {
          background: $color-bg-light;
        }
      }
    }

    .error-message {
      display: block;
      color: $color-danger;
      font-size: 0.8rem;
      margin-top: 0.5rem;
      margin-left: 0.5rem;
    }
  }

  .auth-btn {
    width: 100%;
    padding: 1rem;
    border: none;
    border-radius: 12px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: $transition-base;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;

    &:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }

    &.login-btn {
      background: $gradient-primary;
      color: white;

      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
      }
    }

    &.register-btn {
      background: $gradient-danger;
      color: white;

      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(255, 107, 107, 0.3);
      }
    }

    .loading-spinner {
      width: 16px;
      height: 16px;
      border: 2px solid rgba(255, 255, 255, 0.3);
      border-top: 2px solid white;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .auth-page {
    padding: 1rem;
  }

  .auth-card {
    padding: 2rem;
  }

  .back-to-home-btn {
    top: 1rem;
    left: 1rem;
    padding: 0.5rem 0.8rem;
    font-size: 0.8rem;
  }
}

@media (max-width: $breakpoint-sm) {
  .auth-header .logo {
    flex-direction: column;
    gap: 0.5rem;

    h1 {
      font-size: 1.5rem;
    }
  }
}
</style>
