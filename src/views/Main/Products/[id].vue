<template>
  <div class="product-page">
    <button class="back-btn" @click="router.push('/')">← 返回</button>

    <div class="product-detail">
      <div class="product-detail-content" v-if="product">
        <div class="product-image-large">
          <img :src="product.image" :alt="product.goodName" />
        </div>

        <div class="product-detail-info">
          <h1>{{ product.goodName }}</h1>
          <p class="product-description">{{ product.description }}</p>

          <div class="price-section-large">
            <div class="seckill-price">
              <span class="label">秒杀价</span>
              <span class="price">¥{{ product.price }}</span>
            </div>
            <!-- <div class="original-price-large">原价：¥{{ product.price }}</div> -->
          </div>

          <div class="content-section">
            <h3>商品说明</h3>
            {{ product.content }}
          </div>

          <div class="quantity-section">
            <label>购买数量：</label>
            <div class="quantity-controls">
              <button @click="decreaseQuantity" :disabled="quantity <= 0">-</button>
              <span class="quantity">{{ quantity }}</span>
              <button @click="increaseQuantity" :disabled="quantity >= 5">+</button>
            </div>
            <span class="quantity-limit">限购5件</span>
            <span class="quantity-limit">还剩{{ product.quantity }}件</span>
          </div>

          <div class="action-buttons">
            <button class="btn-seckill" @click="handleBuyNow">🔥 立即购买</button>
            <button class="btn-cart" @click="handleAddToCart">🛒 加入购物车</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getGoodInfo } from '@/apis/goodsApi'
import type { GoodInfo } from '@/types/goods'
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const product = ref<GoodInfo | null>(null)
const quantity = ref(0)

const userStore = useUserStore()
const { isLoggedIn } = storeToRefs(userStore)

const decreaseQuantity = () => {
  if (quantity.value > 0) {
    quantity.value -= 1
  }
}

const increaseQuantity = () => {
  if (quantity.value < 5) {
    quantity.value += 1
  }
}

onMounted(async () => {
  const productId = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id
  if (productId) {
    try {
      const goodId = Number(productId) // Convert to number
      if (isNaN(goodId)) {
        console.error('Invalid product ID', productId)
        product.value = null
        return
      }
      const res = await getGoodInfo(goodId)
      console.log(res.data)

      product.value = res.data
    } catch (error) {
      console.error('Error fetching product detail:', error)
      product.value = null
    }
  }
})

const handleBuyNow = () => {
  if (!isLoggedIn.value) {
    router.push('/login')
    return
  }

  if (quantity.value <= 0) {
    ElMessage.warning('请先添加商品')
    return
  }

  if (product.value) {
    const singleOrderItem = [
      {
        goodId: product.value.goodId,
        goodName: product.value.goodName,
        price: product.value.price,
        quantity: quantity.value,
        image: product.value.image, // Ensure image is passed for display
        description: product.value.description, // Ensure description is passed
      },
    ]
    router.push({
      path: '/order',
      query: { order: JSON.stringify(singleOrderItem) },
    })
  }
}

const handleAddToCart = () => {
  if (!isLoggedIn.value) {
    router.push('/login')
    return
  }

  if (quantity.value <= 0) {
    ElMessage.warning('请先添加商品')
    return
  }

  if (product.value) {
    const itemToAdd = {
      goodId: product.value.goodId,
      goodName: product.value.goodName,
      image: product.value.image,
      price: product.value.price,
      quantity: quantity.value,
      description: product.value.description, // Assuming description is part of GoodBrief
    }
    userStore.addCartItem(itemToAdd)
    alert(`已添加 ${quantity.value} 件 ${product.value?.goodName} 到购物车！`)
    router.push('/cart') // Redirect to cart page
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.product-page {
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

  .product-detail-content {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 3rem;
    background: white;
    padding: 2rem;
    border-radius: $border-radius-xl;
    box-shadow: $shadow-md;

    .product-image-large {
      img {
        width: 100%;
        height: 400px;
        object-fit: cover;
        border-radius: $border-radius-lg;
      }
    }

    .product-detail-info {
      h1 {
        font-size: 2rem;
        margin-bottom: 1rem;
        color: $color-text-primary;
      }

      .product-description {
        color: $color-text-secondary;
        margin-bottom: 2rem;
        line-height: 1.6;
      }

      .price-section-large {
        margin-bottom: 2rem;

        .seckill-price {
          display: flex;
          align-items: center;
          gap: 1rem;
          margin-bottom: 0.5rem;

          .label {
            background: $color-danger;
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: $border-radius-xl;
            font-size: 0.8rem;
          }

          .price {
            font-size: 2.5rem;
            font-weight: bold;
            color: $color-danger;
          }
        }

        .original-price-large {
          color: $color-text-muted;
          text-decoration: line-through;
        }
      }

      .content-section {
        background: $color-bg-light;
        padding: 1.5rem;
        border-radius: $border-radius-lg;
        margin-bottom: 2rem;

        h3 {
          margin-bottom: 1rem;
          color: $color-text-primary;
        }

        .countdown-large {
          font-size: 2rem;
          font-weight: bold;
          color: $color-danger;
          font-family: 'Courier New', monospace;
        }
      }

      .quantity-section {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 2rem;

        label {
          font-weight: bold;
        }

        .quantity-controls {
          display: flex;
          align-items: center;
          border: 2px solid $color-border;
          border-radius: $border-radius-full;
          overflow: hidden;

          button {
            background: $color-bg-light;
            border: none;
            padding: 0.5rem 1rem;
            cursor: pointer;
            font-size: 1.2rem;
            transition: $transition-base;

            &:hover:not(:disabled) {
              background: darken($color-bg-light, 5%);
            }

            &:disabled {
              opacity: 0.5;
              cursor: not-allowed;
            }
          }

          .quantity {
            padding: 0.5rem 1rem;
            font-weight: bold;
            min-width: 50px;
            text-align: center;
          }
        }

        .quantity-limit {
          color: $color-text-secondary;
          font-size: 0.9rem;
        }
      }

      .action-buttons {
        display: flex;
        gap: 1rem;

        button {
          flex: 1;
          padding: 1rem 2rem;
          border: none;
          border-radius: $border-radius-full;
          font-size: 1.1rem;
          font-weight: bold;
          cursor: pointer;
          transition: $transition-base;

          &.btn-seckill {
            background: $gradient-danger;
            color: white;

            &:hover {
              transform: translateY(-2px);
              box-shadow: $shadow-danger;
            }
          }

          &.btn-cart {
            background: $color-primary;
            color: white;

            &:hover {
              background: darken($color-primary, 10%);
              transform: translateY(-2px);
              box-shadow: $shadow-primary;
            }
          }
        }
      }
    }
  }
}

@media (max-width: $breakpoint-md) {
  .product-page {
    .product-detail-content {
      grid-template-columns: 1fr;
      gap: 2rem;
    }

    .action-buttons {
      flex-direction: column;
    }
  }
}
</style>
