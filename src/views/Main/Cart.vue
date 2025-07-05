<template>
  <div class="cart-page">
    <h1>🛒 购物车</h1>
    <div v-if="cartItems.length === 0" class="empty-cart">
      <p>您的购物车是空的。</p>
      <button @click="$router.push('/')" class="btn-primary">继续购物</button>
    </div>
    <div v-else>
      <div class="cart-items">
        <div v-for="item in cartItems" :key="item.goodId" class="cart-item">
          <div class="item-image">
            <img :src="item.image" :alt="item.goodName" />
          </div>
          <div class="item-details">
            <h3>{{ item.goodName }}</h3>
            <p class="item-price">¥{{ item.price }}</p>
            <div class="item-controls">
              <div class="item-quantity-controls">
                <button
                  @click="updateQuantity(item.goodId, item.quantity - 1)"
                  :disabled="item.quantity <= 1"
                >
                  -
                </button>
                <span>{{ item.quantity }}</span>
                <button
                  @click="updateQuantity(item.goodId, item.quantity + 1)"
                  :disabled="item.quantity >= 5"
                >
                  +
                </button>
              </div>
              <button @click="removeFromCart(item.goodId)" class="remove-btn">移除</button>
            </div>
          </div>
        </div>
      </div>
      <div class="cart-summary">
        <p>总计：¥{{ totalPrice.toFixed(2) }}</p>
        <button class="btn-checkout" @click="goToCheckout">去结算</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useUserStore } from '@/stores/userStore'
import type { CartItem } from '@/types/goods'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const cartItems = computed<CartItem[]>(() => userStore.cartItems)

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const updateQuantity = (goodId: number, newQuantity: number) => {
  userStore.updateCartItemQuantity(goodId, newQuantity)
}

const removeFromCart = (goodId: number) => {
  userStore.removeCartItem(goodId)
}

const goToCheckout = () => {
  if (cartItems.value.length === 0) {
    return
  }

  router.push({
    path: '/order',
    query: { order: JSON.stringify(cartItems.value) },
  })
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.cart-page {
  padding: 20px;

  h1 {
    text-align: center;
    margin-bottom: 2rem;
    color: $color-text-primary;
  }

  .empty-cart {
    text-align: center;
    padding: 50px;
    border: 1px dashed $color-border;
    border-radius: $border-radius-lg;
    background: $color-bg-light;

    p {
      font-size: 1.2rem;
      color: $color-text-secondary;
      margin-bottom: 1.5rem;
    }

    .btn-primary {
      background: $color-primary;
      color: white;
      border: none;
      padding: 0.8rem 1.5rem;
      border-radius: $border-radius-full;
      cursor: pointer;
      font-size: 1rem;
      transition: $transition-base;

      &:hover {
        background: darken($color-primary, 10%);
      }
    }
  }

  .cart-items {
    display: grid;
    gap: 20px;
    margin-bottom: 2rem;
  }

  .cart-item {
    display: flex;
    align-items: center;
    background: white;
    padding: 1.5rem;
    border-radius: $border-radius-xl;
    box-shadow: $shadow-sm;
    gap: 1.5rem;

    .item-image {
      width: 100px;
      height: 100px;
      flex-shrink: 0;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: $border-radius-md;
      }
    }

    .item-details {
      flex-grow: 1;

      h3 {
        font-size: 1.2rem;
        margin-bottom: 0.5rem;
        color: $color-text-primary;
      }

      .item-price {
        font-size: 1.1rem;
        color: $color-danger;
        font-weight: bold;
        margin-bottom: 1rem;
      }

      .item-controls {
        display: flex;
        gap: 1rem;
      }

      .item-quantity-controls {
        display: flex;
        align-items: center;
        border: 1px solid $color-border;
        border-radius: $border-radius-full;
        overflow: hidden;
        width: fit-content;

        button {
          background: $color-bg-light;
          border: none;
          padding: 0.3rem 0.8rem;
          cursor: pointer;
          font-size: 1rem;
          transition: $transition-base;

          &:hover:not(:disabled) {
            background: darken($color-bg-light, 5%);
          }

          &:disabled {
            opacity: 0.5;
            cursor: not-allowed;
          }
        }

        span {
          padding: 0.3rem 0.8rem;
          font-weight: bold;
          min-width: 30px;
          text-align: center;
        }
      }

      .remove-btn {
        background: $gradient-danger;
        color: white;
        border: none;
        padding: 0.5rem 1rem;
        border-radius: $border-radius-md;
        cursor: pointer;
        font-size: 0.9rem;
        transition: $transition-base;

        &:hover {
          background: $shadow-danger;
        }
      }
    }
  }

  .cart-summary {
    background: white;
    padding: 1.5rem;
    border-radius: $border-radius-xl;
    box-shadow: $shadow-md;
    text-align: right;

    p {
      font-size: 1.5rem;
      font-weight: bold;
      color: $color-danger;
      margin-bottom: 1.5rem;
    }

    .btn-checkout {
      background: $gradient-success;
      color: white;
      border: none;
      padding: 1rem 2rem;
      border-radius: $border-radius-full;
      cursor: pointer;
      font-size: 1.2rem;
      font-weight: bold;
      transition: $transition-base;

      &:hover {
        transform: translateY(-2px);
        box-shadow: $shadow-success;
      }
    }
  }
}
</style>
