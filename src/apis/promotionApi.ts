import apiClient from "@/utils/apiClient";
import type { PromotionListResponse, PromotionDetail } from '@/types/promotion'

// 获取活动列表，choice = 1, 2分别为按照ID、按照startTime排序
export const getPromotionList = (choice: number = 1, limit: number = 5): Promise<PromotionListResponse> => {
  return apiClient.get('/promotion/list', { params: { choice, limit } });
}

// 获取详细活动信息
export const getPromotionDetail = (promotionId: number): Promise<PromotionDetail> => {
  return apiClient.get(`/promotion/${promotionId}`);
}
