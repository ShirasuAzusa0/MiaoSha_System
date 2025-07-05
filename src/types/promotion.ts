import type { GoodBrief } from "./goods"

export interface PromotionListResponse {
  /**
   * 状态说明
   */
  msg: string
  /**
   * 活动列表
   */
  promotions: Promotion[]
  /**
   * 状态
   */
  status: string
}

/**
 * 活动简要信息
 */
export interface Promotion {
  /**
   * 活动结束时间
   */
  endTime: string
  /**
   * 活动名称
   */
  promotionName: string
  /**
   * 活动ID
   */
  promotionId: number
  /**
   * 活动开始时间
   */
  startTime: string
}

/**
 * 活动信息
 */
export interface PromotionDetail extends Promotion {
  /**
   * 内容
   */
  content: string
  /**
   * 活动Id
   */
  promotionId: number
  /**
   * 活动商品列表
   */
  goods: GoodBrief[]
}
