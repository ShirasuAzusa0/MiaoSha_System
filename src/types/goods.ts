// 获取具体商品信息响应
export interface GoodResponse {
  data: GoodInfo
  /**
   * 状态说明
   */
  msg: string
  /**
   * 状态
   */
  status: string
}

// 具体商品信息
export interface GoodInfo {
  /**
   * 详细内容
   */
  content: string
  /**
   * 商品说明
   */
  description: string
  /**
   * ID 编号
   */
  goodId: number
  /**
   * 名称
   */
  goodName: string
  /**
   * 商品图集
   */
  graphs?: string[]
  /**
   * 商品图片
   */
  image: string
  /**
   * 商品价格
   */
  price: number
  /**
   * 商品数量
   */
  quantity: number
  /**
   * 标签
   */
  tags: string[]
}

export interface GoodsListResponse {
  /**
   * 商品列表
   */
  goods: GoodBrief[]
  /**
   * 状态说明
   */
  msg: string
  /**
   * 状态
   */
  status: string
}

/**
 * 商品简要信息
 */
export interface GoodBrief {
  /**
   * 商品ID
   */
  goodId: number
  /**
   * 商品简介
   */
  description: string
  /**
   * 商品名
   */
  goodName: string
  /**
   * 商品图片
   */
  image: string
  /**
   * 商品价格
   */
  price: number
  /**
   * 商品数量
   */
  quantity: number
}

export interface CartItem extends GoodBrief {
  quantity: number
}

// 提交订单返回信息
export interface OrderResponse {
  /**
   * 邮寄地址
   */
  address: string;
  /**
   * 买家ID
   */
  customerId: number;
  /**
   * 购物车
   */
  data: OrderData[];
  /**
   * 状态说明
   */
  msg: string;
  /**
   * 订单号
   */
  orderId: number;
  /**
   * 下单时间
   */
  orderTime: string;
  /**
   * 状态
   */
  status: string;
  /**
   * 总消费
   */
  totalCost: string;
}

/**
* 购物车各个商品
*/
export interface OrderData {
  /**
   * 单种商品总花销
   */
  cost?: number;
  /**
   * 商品ID
   */
  goodId: number;
  /**
   * 商品数量
   */
  quantity: number;
}

export interface TagsResponse {
  /**
   * 标签列表
   */
  List: TagList[]
  /**
   * 状态说明
   */
  msg: string
  /**
   * 状态
   */
  status: string
}

export interface TagList {
  /**
   * 标签名
   */
  tagName: string
}
