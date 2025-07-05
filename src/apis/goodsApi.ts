import type {
  GoodsListResponse,
  GoodResponse,
  OrderResponse,
  TagsResponse,
  OrderData,
} from '@/types/goods'
import apiClient from '@/utils/apiClient'

// 商品展示
// 获取商品列表, choice = 1, 2, 3分别为按照ID、按照price、按照quantity排序
export const getGoodsList = (
  tag: string,
  choice: number = 1,
  limit: number = 5,
): Promise<GoodsListResponse> => {
  return apiClient.get(`/goods/list/${tag}`, { params: { choice, limit } })
}

// 获取具体商品信息
export const getGoodInfo = (goodId: number): Promise<GoodResponse> => {
  return apiClient.get(`/goods/${goodId}`)
}

// 获取标签列表
export const getTagList = (): Promise<TagsResponse> => {
  return apiClient.get('/goods/tag-list')
}

// 商品抢购
// 提交订单
export const submitOrder = (goods: OrderData[], address: string): Promise<OrderResponse> => {
  return apiClient.post('/good/order', { goods, address })
}
