<template>
  <div class="profile-page">
    <h2>👤 个人中心</h2>

    <div class="profile-content" v-if="userStore.user">
      <div class="profile-info">
        <div class="avatar-section">
          <div class="avatar">
            <img :src="userStore.user.avatar" :alt="userStore.user.userName" />
          </div>
          <button class="change-avatar-btn">更换头像</button>
        </div>

        <div class="info-form">
          <h3>基本信息</h3>
          <div class="form-group">
            <label>用户名：</label>
            <input
              :value="localUserInfo.userName"
              @input="updateUserInfo('userName', ($event.target as HTMLInputElement).value)"
              :disabled="!isEditing"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>邮箱：</label>
            <input
              :value="localUserInfo.email"
              @input="updateUserInfo('email', ($event.target as HTMLInputElement).value)"
              :disabled="!isEditing"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>自我介绍：</label>
            <textarea
              :value="localUserInfo.self_description"
              @input="
                updateUserInfo('self_description', ($event.target as HTMLTextAreaElement).value)
              "
              :disabled="!isEditing"
              class="form-textarea"
            ></textarea>
          </div>

          <div class="form-actions">
            <button v-if="!isEditing" @click="startEdit" class="edit-btn">✏️ 编辑信息</button>
            <template v-else>
              <button @click="saveUserInfo" class="save-btn">💾 保存</button>
              <button @click="cancelEdit" class="cancel-btn">❌ 取消</button>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'
import type { UserInfo } from '@/types/userInfo'

const route = useRoute()
const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const isEditing = ref(false)
const localUserInfo = reactive<Partial<UserInfo>>({})
const originalUserInfo = ref<Partial<UserInfo>>({})

// Watch for changes in userStore.user and update localUserInfo
watch(
  user,
  (newVal) => {
    if (newVal) {
      Object.assign(localUserInfo, newVal)
    }
  },
  { immediate: true, deep: true },
)

const updateUserInfo = (field: 'userName' | 'email' | 'self_description', value: string) => {
  localUserInfo[field] = value
}

const startEdit = () => {
  originalUserInfo.value = { ...localUserInfo }
  isEditing.value = true
}

const saveUserInfo = async () => {
  if (!userStore.user) {
    ElMessage.error('用户未登录，无法保存信息')
    return
  }

  try {
    // Ensure all required fields for UserInfo are present before sending
    // This might need more robust validation based on your backend
    const updatedInfo: UserInfo = { ...userStore.user, ...(localUserInfo as UserInfo) }
    await userStore.editUserInfo(updatedInfo)
    ElMessage.success('个人信息保存成功！')
    isEditing.value = false
  } catch (error) {
    ElMessage.error('保存失败，请重试！')
    console.error('Error saving user info:', error)
  }
}

const cancelEdit = () => {
  Object.assign(localUserInfo, originalUserInfo.value)
  isEditing.value = false
}

onMounted(async () => {
  const userId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  if (!user.value) {
    try {
      await userStore.fetchUserInfo(userId)
    } catch (error) {
      console.error('Error fetching user info:', error)
      ElMessage.error('获取用户信息失败')
    }
  } else {
    Object.assign(localUserInfo, user.value)
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.profile-page {
  h2 {
    margin-bottom: 2rem;
    color: $color-text-primary;
  }

  .profile-content {
    display: flex;
    flex-direction: column;

    .profile-info {
      background: white;
      padding: 2rem;
      border-radius: $border-radius-xl;
      box-shadow: $shadow-md;

      .avatar-section {
        text-align: center;
        margin-bottom: 2rem;

        .avatar {
          width: 100px;
          height: 100px;
          border-radius: 50%;
          overflow: hidden;
          margin: 0 auto 1rem;
          border: 4px solid $color-primary;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .change-avatar-btn {
          background: $color-primary;
          color: white;
          border: none;
          padding: 0.5rem 1rem;
          border-radius: $border-radius-lg;
          cursor: pointer;
          transition: $transition-base;

          &:hover {
            background: darken($color-primary, 10%);
          }
        }
      }

      .info-form {
        h3 {
          margin-bottom: 1.5rem;
          color: $color-text-primary;
        }

        .form-group {
          margin-bottom: 1.5rem;

          label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
            color: $color-text-primary;
          }

          .form-input,
          .form-textarea {
            width: 100%;
            padding: 1rem;
            border: 2px solid $color-border;
            border-radius: $border-radius-md;
            font-size: 1rem;
            transition: $transition-base;

            &:focus {
              outline: none;
              border-color: $color-primary;
            }

            &:disabled {
              background: $color-bg-light;
              color: $color-text-secondary;
            }
          }

          .form-textarea {
            min-height: 80px;
            resize: vertical;
          }
        }

        .form-actions {
          display: flex;
          gap: 1rem;

          button {
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: $border-radius-full;
            cursor: pointer;
            font-weight: bold;
            transition: $transition-base;

            &.edit-btn {
              background: $color-primary;
              color: white;

              &:hover {
                background: darken($color-primary, 10%);
              }
            }

            &.save-btn {
              background: $color-success;
              color: white;

              &:hover {
                background: darken($color-success, 10%);
              }
            }

            &.cancel-btn {
              background: $color-danger;
              color: white;

              &:hover {
                background: darken($color-danger, 10%);
              }
            }
          }
        }
      }
    }

    .order-history {
      background: white;
      padding: 2rem;
      border-radius: $border-radius-xl;
      box-shadow: $shadow-md;

      h3 {
        margin-bottom: 1.5rem;
        color: $color-text-primary;
      }

      .order-list {
        .order-item {
          border: 1px solid $color-border;
          border-radius: $border-radius-lg;
          padding: 1.5rem;
          margin-bottom: 1rem;

          .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;

            .order-id {
              font-weight: bold;
              color: $color-text-primary;
            }

            .order-status {
              padding: 0.3rem 0.8rem;
              border-radius: $border-radius-xl;
              font-size: 0.8rem;
              font-weight: bold;

              &.delivered {
                background: $color-success;
                color: white;
              }

              &.shipping {
                background: $color-warning;
                color: white;
              }

              &.processing {
                background: $color-primary;
                color: white;
              }
            }
          }

          .order-details {
            display: flex;
            gap: 1rem;

            img {
              width: 60px;
              height: 60px;
              object-fit: cover;
              border-radius: $border-radius-md;
            }

            .order-info {
              h4 {
                margin-bottom: 0.5rem;
                color: $color-text-primary;
              }

              p {
                color: $color-text-secondary;
                margin-bottom: 0.3rem;
              }

              .order-price {
                font-weight: bold;
                color: $color-danger;
              }
            }
          }
        }
      }
    }
  }
}

@media (max-width: $breakpoint-md) {
  .profile-page {
    .profile-content {
      grid-template-columns: 1fr;
      gap: 2rem;
    }

    .form-actions {
      flex-direction: column;
    }
  }
}
</style>
