<template>
  <div class="order-page">
    <button class="back-btn" @click="router.push('/')">← 返回</button>

    <h2>📋 确认订单</h2>

    <div class="order-content" v-if="orderItems.length > 0">
      <div class="order-info">
        <div class="product-summary">
          <h3>商品信息</h3>
          <div class="order-item" v-for="item in orderItems" :key="item.goodId">
            <img :src="item.image" />
            <div class="item-details">
              <h4>{{ item.goodName }}</h4>
              <p>数量：{{ item.quantity }}</p>
              <p class="item-price">¥{{ item.price }} × {{ item.quantity }}</p>
            </div>
          </div>
        </div>

        <div class="address-section">
          <h3>收货地址</h3>
          <div class="address-form">
            <input
              v-model="orderForm.name"
              @input="updateForm('name', ($event.target as HTMLInputElement).value)"
              placeholder="收货人姓名"
            />
            <input
              v-model="orderForm.phone"
              @input="updateForm('phone', ($event.target as HTMLInputElement).value)"
              placeholder="联系电话"
            />
            <textarea
              v-model="orderForm.address"
              @input="updateForm('address', ($event.target as HTMLTextAreaElement).value)"
              placeholder="详细地址"
            ></textarea>
          </div>
        </div>
      </div>

      <div class="order-summary">
        <h3>订单摘要</h3>
        <div class="summary-item">
          <span>商品总价：</span>
          <span>¥{{ totalProductPrice.toFixed(2) }}</span>
        </div>
        <div class="summary-item">
          <span>运费：</span>
          <span>¥0.00</span>
        </div>
        <div class="summary-total">
          <span>总计：</span>
          <span class="total-price">¥{{ totalProductPrice.toFixed(2) }}</span>
        </div>

        <button class="submit-order-btn" @click="handleSubmitOrder">💳 提交订单</button>
      </div>
    </div>

    <OrderConfirmationModal
      v-if="showOrderConfirmationModal"
      :orderData="confirmedOrderData!"
      @close="handleCloseModal"
    />
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import type { OrderData, CartItem, OrderResponse } from '@/types/goods'
import { ref, onMounted, reactive, computed } from 'vue'
import { submitOrder } from '@/apis/goodsApi'
import { ElMessage } from 'element-plus'
import OrderConfirmationModal from '@/components/OrderConfirmationModal.vue'
import { useUserStore } from '@/stores/userStore'

const orderItems = ref<CartItem[]>([]) // Use CartItem to store full product info for display
const orderForm = reactive({
  name: '',
  phone: '',
  address: '',
})

const showOrderConfirmationModal = ref(false)
const confirmedOrderData = ref<OrderResponse | null>(null)

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const totalProductPrice = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const updateForm = (field: 'name' | 'phone' | 'address', value: string) => {
  orderForm[field] = value
}

const handleSubmitOrder = async () => {
  if (orderItems.value.length === 0) {
    ElMessage.error('订单信息为空')
    return
  }

  if (!orderForm.name || !orderForm.phone || !orderForm.address) {
    ElMessage.warning('请填写完整的收货信息')
    return
  }

  try {
    // Map CartItem to OrderData for the API call
    const orderDataForApi: OrderData[] = orderItems.value.map((item) => ({
      goodId: item.goodId,
      quantity: item.quantity,
    }))

    const response = await submitOrder(orderDataForApi, orderForm.address)
    ElMessage.success('订单提交成功！')
    confirmedOrderData.value = response // Assign the entire response object
    showOrderConfirmationModal.value = true

    // Clear the cart after successful submission
    userStore.clearCart()
  } catch (error) {
    ElMessage.error('订单提交失败，请重试')
    console.error('Order submission error:', error)
  }
}

const handleCloseModal = () => {
  showOrderConfirmationModal.value = false
  router.push('/') // Redirect to home after closing the modal
}

onMounted(() => {
  const orderQuery = route.query.order as string
  if (orderQuery) {
    try {
      const parsedOrder: CartItem[] = JSON.parse(orderQuery)
      orderItems.value = parsedOrder
    } catch (e) {
      ElMessage.error('解析订单信息失败')
      console.error('Failed to parse order query:', e)
      router.push('/')
    }
  } else {
    ElMessage.error('缺少订单信息，请从购物车进入')
    router.push('/')
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.order-page {
  .back-btn {
    background: $color-primary;
    color: white;
    border: none;
    padding: 0.8rem 1.5rem;
    border-radius: $border-radius-full;
    cursor: pointer;
    margin-bottom: 2rem;
    font-size: 1rem;
    transition: $transition-base;

    &:hover {
      background: darken($color-primary, 10%);
      transform: translateX(-5px);
    }
  }

  h2 {
    margin-bottom: 2rem;
    color: $color-text-primary;
  }

  .order-content {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 3rem;

    .order-info {
      .product-summary,
      .address-section {
        background: white;
        padding: 2rem;
        border-radius: $border-radius-xl;
        box-shadow: $shadow-md;
        margin-bottom: 2rem;

        h3 {
          margin-bottom: 1rem;
          color: $color-text-primary;
        }
      }

      .order-item {
        display: flex;
        gap: 1rem;
        padding: 1rem;
        background: $color-bg-light;
        border-radius: $border-radius-lg;

        img {
          width: 80px;
          height: 80px;
          object-fit: cover;
          border-radius: $border-radius-md;
        }

        .item-details {
          h4 {
            margin-bottom: 0.5rem;
            color: $color-text-primary;
          }

          p {
            color: $color-text-secondary;
            margin-bottom: 0.3rem;
          }

          .item-price {
            font-weight: bold;
            color: $color-danger;
          }
        }
      }

      .address-form {
        display: flex;
        flex-direction: column;
        gap: 1rem;

        input,
        textarea {
          padding: 1rem;
          border: 2px solid $color-border;
          border-radius: $border-radius-md;
          font-size: 1rem;
          transition: $transition-base;

          &:focus {
            outline: none;
            border-color: $color-primary;
          }
        }

        textarea {
          min-height: 100px;
          resize: vertical;
        }
      }
    }

    .order-summary {
      background: $color-bg-light;
      padding: 2rem;
      border-radius: $border-radius-xl;
      height: fit-content;
      box-shadow: $shadow-md;

      h3 {
        margin-bottom: 1.5rem;
        color: $color-text-primary;
      }

      .summary-item {
        display: flex;
        justify-content: space-between;
        margin-bottom: 1rem;
        color: $color-text-secondary;
      }

      .summary-total {
        display: flex;
        justify-content: space-between;
        padding-top: 1rem;
        border-top: 2px solid $color-border;
        font-weight: bold;
        font-size: 1.2rem;

        .total-price {
          color: $color-danger;
        }
      }

      .submit-order-btn {
        width: 100%;
        background: $gradient-danger;
        color: white;
        border: none;
        padding: 1rem;
        border-radius: $border-radius-full;
        font-size: 1.1rem;
        font-weight: bold;
        cursor: pointer;
        margin-top: 2rem;
        transition: $transition-base;

        &:hover {
          transform: translateY(-2px);
          box-shadow: $shadow-danger;
        }
      }
    }
  }
}

@media (max-width: $breakpoint-md) {
  .order-page {
    .order-content {
      grid-template-columns: 1fr;
      gap: 2rem;
    }
  }
}
</style>

<OrderConfirmationModal
  v-if="showOrderConfirmationModal"
  :orderData="confirmedOrderData!"
  @close="handleCloseModal"
/>
