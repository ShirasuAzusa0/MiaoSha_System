<template>
  <div class="home-page">
    <!-- 活动横幅 -->
    <div class="banner">
      <div class="banner-content">
        <h1>🔥 限时秒杀活动</h1>
        <p>超值商品，限量抢购！</p>
      </div>
    </div>

    <div class="section-header">
      <h2>热门活动</h2>
      <div class="sort-options">
        <button @click="sortPromotions(1)" :class="{ active: currentPromotionSort === 1 }">
          按ID排序
        </button>
        <button @click="sortPromotions(2)" :class="{ active: currentPromotionSort === 2 }">
          按开始时间排序
        </button>
      </div>
    </div>
    <div class="activity-grid">
      <div
        v-for="activity in activities"
        :key="activity.promotionName"
        class="activity-card"
        @click="handleCardClick(activity.promotionId)"
      >
        <div class="activity-info">
          <h3>{{ activity.promotionName }}</h3>
          <p class="activity-desc">开始于{{ activity.startTime }}</p>
          <p class="activity-desc">结束时间：{{ activity.endTime }}</p>
        </div>
      </div>
    </div>

    <div class="products-section">
      <div class="section-header">
        <h2>商品列表</h2>
        <div class="sort-options">
          <button @click="sortGoods(1)" :class="{ active: currentGoodSort === 1 }">按ID排序</button>
          <button @click="sortGoods(2)" :class="{ active: currentGoodSort === 2 }">
            按价格排序
          </button>
          <button @click="sortGoods(3)" :class="{ active: currentGoodSort === 3 }">
            按数量排序
          </button>
        </div>
      </div>
      <div class="tag-list">
        <button
          v-for="tag of tagList"
          :key="tag.tagName"
          @click="selectTag(tag.tagName)"
          :class="{ active: currentTag === tag.tagName }"
        >
          {{ tag.tagName }}
        </button>
      </div>
      <div class="products-grid">
        <div
          v-for="product in products"
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

    <!-- Promotion Detail Card -->
    <PromotionDetailCard
      v-if="showPromotionDetail"
      :promotionId="selectedPromotionId"
      @close="handleClosePromotionDetail"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPromotionList } from '@/apis/promotionApi'
import { getGoodsList, getTagList } from '@/apis/goodsApi'

import type { Promotion } from '@/types/promotion'
import type { GoodBrief, TagList } from '@/types/goods'

import PromotionDetailCard from '@/components/PromotionDetailCard.vue'

const activities = ref<Promotion[]>([])

const products = ref<GoodBrief[]>([])
const showPromotionDetail = ref(false)
const selectedPromotionId = ref<number | null>(null)
const currentPromotionSort = ref(1)
const currentGoodSort = ref(1)
const currentTag = ref('')

const tagList = ref<TagList[]>()

const handleCardClick = (promotionId: number) => {
  selectedPromotionId.value = promotionId
  showPromotionDetail.value = true
}

const handleClosePromotionDetail = () => {
  showPromotionDetail.value = false
  selectedPromotionId.value = null // Reset selected ID on close
}

const fetchPromotions = async () => {
  try {
    const promotionsResponse = await getPromotionList(currentPromotionSort.value)
    activities.value = promotionsResponse.promotions
  } catch (error) {
    console.error('Error fetching promotions:', error)
  }
}

const fetchGoods = async () => {
  try {
    const goodsResponse = await getGoodsList(currentTag.value, currentGoodSort.value)
    products.value = goodsResponse.goods
  } catch (error) {
    console.error('Error fetching goods:', error)
  }
}

const fetchTagList = async () => {
  try {
    const tagListResponse = await getTagList()
    tagList.value = tagListResponse.List
    if (tagList.value.length > 0) {
      currentTag.value = tagList.value[0].tagName // Set the first tag as the default selected tag
    }
  } catch (error) {
    console.error('Error fetching tag list:', error)
  }
}

const sortPromotions = (sortOption: number) => {
  currentPromotionSort.value = sortOption
  fetchPromotions()
}

const sortGoods = (sortOption: number) => {
  currentGoodSort.value = sortOption
  fetchGoods()
}

const selectTag = (tagName: string) => {
  currentTag.value = tagName
  fetchGoods()
}

onMounted(async () => {
  try {
    await fetchPromotions()
    await fetchGoods()
    await fetchTagList()
  } catch (error) {
    console.error('Error fetching data:', error)
  }
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.home-page {
  padding: 20px;

  .banner {
    background: $gradient-danger;
    color: white;
    padding: 3rem 2rem;
    border-radius: $border-radius-xl;
    margin-bottom: 3rem;
    text-align: center;

    .banner-content h1 {
      font-size: 2.5rem;
      margin-bottom: 1rem;
    }

    .banner-content p {
      font-size: 1.2rem;
    }
  }

  h1 {
    text-align: center;
    margin-bottom: 20px;
  }

  h2 {
    font-size: 2rem;
    margin-bottom: 0.5rem;
    color: $color-text-primary;
  }

  .section-header {
    display: flex;
    width: 100%;
    justify-content: space-between;
    border-bottom: 1px solid #eee;
    margin-bottom: 1rem;
    margin-top: 3rem;
  }

  .sort-options {
    margin-bottom: 0.5rem;
    display: flex;
    gap: 10px;
    height: 3rem;

    button {
      padding: 0.5rem 1rem;
      border: 1px solid $color-border;
      background: $color-bg-light;
      color: $color-text-secondary;
      cursor: pointer;
      transition: $transition-base;
      border-radius: $border-radius-md;

      &.active {
        background: $color-primary;
        color: white;
        border-color: $color-primary;
      }

      &:hover {
        background: $color-primary-light;
        color: white;
        border-color: $color-primary-light;
      }
    }
  }

  .activity-grid {
    display: grid;
    gap: 20px;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }

  .activity-card {
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

    .activity-image {
      position: relative;
      height: 200px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .activity-info {
      padding: 1.5rem;

      h3 {
        font-size: 1.3rem;
        margin-bottom: 1rem;
        color: $color-text-primary;
      }

      .activity-desc {
        color: $color-text-secondary;
        margin-bottom: 1rem;
        font-size: 0.9rem;
      }
    }
  }

  .products-section {
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
  }

  .tag-list {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;

    button {
      padding: 0.5rem 1rem;
      border: 1px solid $color-border;
      background: $color-bg-light;
      color: $color-text-secondary;
      cursor: pointer;
      transition: $transition-base;
      border-radius: $border-radius-md;

      &.active {
        background: $color-primary;
        color: white;
        border-color: $color-primary;
      }

      &:hover {
        background: $color-primary-light;
        color: white;
        border-color: $color-primary-light;
      }
    }
  }
}
</style>
