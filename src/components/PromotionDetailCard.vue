<template>
  <div class="promotion-detail-overlay" @click.self="handleClose">
    <div class="promotion-detail-card" @click.stop>
      <button class="close-btn" @click="handleClose">&times;</button>
      <div v-if="loading" class="loading-state">加载中...</div>
      <div v-else-if="error" class="error-state">加载失败: {{ error }}</div>
      <div v-else-if="promotion">
        <h2>{{ promotion.promotionName }}</h2>
        <p><strong>开始时间:</strong> {{ new Date(promotion.startTime).toLocaleString() }}</p>
        <p><strong>结束时间:</strong> {{ new Date(promotion.endTime).toLocaleString() }}</p>
        <p><strong>活动内容:</strong> {{ promotion.content }}</p>
        <div class="products-grid" v-if="promotion.goods">
          <div
            v-for="product in promotion.goods"
            :key="product.goodId"
            class="product-card"
            @click="$router.push(`/products/${product.goodId}`)"
          >
            <div class="product-image">
              <img :src="product.image" :alt="product.goodName" />
            </div>
            <div class="product-info">
              <h3>{{ product.goodName }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="price-section">
                <span class="current-price">¥{{ product.price }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="no-data-state">没有找到活动信息。</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPromotionDetail } from '@/apis/promotionApi'
import type { PromotionDetail } from '@/types/promotion'

const props = defineProps<{
  promotionId: number | null
}>()

const emit = defineEmits(['close'])

const promotion = ref<PromotionDetail | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const handleClose = () => {
  emit('close')
}

const fetchPromotionDetails = async (id: number) => {
  loading.value = true
  error.value = null
  try {
    const data = await getPromotionDetail(id)
    promotion.value = data
  } catch (err) {
    console.error('Failed to fetch promotion details:', err)
    error.value = err instanceof Error ? err.message : '未知错误'
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchPromotionDetails(props.promotionId as number))
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.promotion-detail-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.promotion-detail-card {
  background: white;
  padding: 2.5rem;
  border-radius: $border-radius-xl;
  box-shadow: $shadow-xl;
  max-width: 600px;
  width: 90%;
  position: relative;
  max-height: 90%;
  overflow-y: auto;

  .close-btn {
    position: absolute;
    top: 1rem;
    right: 1rem;
    background: none;
    border: none;
    font-size: 2rem;
    cursor: pointer;
    color: $color-text-secondary;
    &:hover {
      color: $color-text-primary;
    }
  }

  h2 {
    color: $color-primary;
    margin-bottom: 1.5rem;
    text-align: center;
  }

  p {
    margin-bottom: 0.8rem;
    line-height: 1.6;
    color: $color-text-primary;
  }

  strong {
    color: $color-text-secondary;
  }

  .loading-state,
  .error-state,
  .no-data-state {
    text-align: center;
    padding: 2rem 0;
    color: $color-text-secondary;
  }

  .error-state {
    color: $color-danger;
  }
}

.products-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
}

.product-card {
  background: white;
  border-radius: $border-radius-xl;
  overflow: hidden;
  box-shadow: $shadow-md;
  transition: $transition-base;
  cursor: pointer;

  &:hover {
    transform: translateY(-5px);
    box-shadow: $shadow-lg;
  }

  .product-image {
    position: relative;
    height: 200px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .discount-badge {
      position: absolute;
      top: 10px;
      right: 10px;
      background: $color-danger;
      color: white;
      padding: 0.3rem 0.8rem;
      border-radius: $border-radius-xl;
      font-size: 0.8rem;
      font-weight: bold;
    }
  }

  .product-info {
    padding: 1.5rem;

    h3 {
      font-size: 1.3rem;
      margin-bottom: 0.5rem;
      color: $color-text-primary;
    }

    .product-desc {
      color: $color-text-secondary;
      margin-bottom: 1rem;
      font-size: 0.9rem;
    }

    .price-section {
      display: flex;
      align-items: center;
      gap: 1rem;

      .current-price {
        font-size: 1.5rem;
        font-weight: bold;
        color: $color-danger;
      }

      .original-price {
        font-size: 1rem;
        color: $color-text-muted;
        text-decoration: line-through;
      }
    }

    .stock-info {
      .stock-bar {
        background: $color-bg-light;
        height: 8px;
        border-radius: 4px;
        overflow: hidden;
        margin-bottom: 0.5rem;

        .stock-progress {
          background: $gradient-danger;
          height: 100%;
          transition: $transition-base;
        }
      }

      .stock-text {
        font-size: 0.8rem;
        color: $color-text-secondary;
      }
    }
  }
}
</style>
