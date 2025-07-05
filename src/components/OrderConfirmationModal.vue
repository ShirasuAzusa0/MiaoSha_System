<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="modal-content">
      <h2>🎉 订单提交成功！</h2>
      <p>您的订单已成功提交。以下是您的订单详情：</p>

      <div class="order-details">
        <p><strong>订单号:</strong> {{ orderData.orderId }}</p>
        <p><strong>收货地址:</strong> {{ orderData.address || '未提供' }}</p>
        <p><strong>总消费:</strong> ¥{{ parseFloat(orderData.totalCost).toFixed(2) }}</p>
        <p><strong>订单提交时间:</strong> {{ new Date(orderData.orderTime).toLocaleString() }}</p>

        <h3>商品明细:</h3>
        <div v-for="(item, index) in orderData.data" :key="index" class="order-item-detail">
          <p><strong>商品ID:</strong> {{ item.goodId }}</p>
          <p><strong>购买数量:</strong> {{ item.quantity }}</p>
          <p v-if="item.cost"><strong>单种商品总花销:</strong> ¥{{ item.cost.toFixed(2) }}</p>
        </div>
      </div>

      <button class="close-btn" @click="handleClose">关闭</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, onMounted } from 'vue'
import type { OrderResponse } from '@/types/goods'

const props = defineProps<{
  orderData: OrderResponse
}>()

const emit = defineEmits(['close'])

const handleClose = () => {
  emit('close')
}

onMounted(() => {
  try {
    const existingOrders = JSON.parse(localStorage.getItem('userOrders') || '[]')
    existingOrders.push(props.orderData)
    localStorage.setItem('userOrders', JSON.stringify(existingOrders))
    console.log('Order saved to local storage:', props.orderData)
  } catch (e) {
    console.error('Failed to save order to local storage:', e)
  }
})
</script>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2.5rem;
  border-radius: $border-radius-xl;
  box-shadow: $shadow-lg;
  text-align: center;
  max-width: 500px;
  width: 90%;
  position: relative;
  transform: translateY(-20px);
  opacity: 0;
  animation: slideIn 0.3s forwards ease-out;

  @keyframes slideIn {
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }

  h2 {
    color: $color-primary;
    margin-bottom: 1.5rem;
    font-size: 2rem;
  }

  p {
    color: $color-text-secondary;
    margin-bottom: 1rem;
    font-size: 1.1rem;
  }

  .order-details {
    background: $color-bg-light;
    padding: 1.5rem;
    border-radius: $border-radius-lg;
    margin-top: 2rem;
    margin-bottom: 2rem;
    text-align: left;

    p {
      margin-bottom: 0.8rem;
      color: $color-text-primary;

      &:last-child {
        margin-bottom: 0;
      }

      strong {
        color: $color-text-primary;
      }
    }

    .order-item-detail {
      border-top: 1px dashed $color-border;
      padding-top: 1rem;
      margin-top: 1rem;

      &:first-of-type {
        border-top: none;
        padding-top: 0;
        margin-top: 0;
      }
    }
  }

  .close-btn {
    background: $color-primary;
    color: white;
    border: none;
    padding: 0.8rem 2rem;
    border-radius: $border-radius-full;
    cursor: pointer;
    font-size: 1.1rem;
    transition: $transition-base;
    margin-top: 1.5rem;

    &:hover {
      background: darken($color-primary, 10%);
      transform: translateY(-2px);
    }
  }
}
</style>
