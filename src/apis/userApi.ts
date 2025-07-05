import apiClient from "@/utils/apiClient";
import type { UserInfo } from "@/types/userInfo";

// 获取个人信息
export const getUserInfo = (userId: string) => {
  return apiClient.get(`/user/profile/${userId}`);
}

// 修改个人信息
export const updateUserInfo = (userInfo: UserInfo) => {
  return apiClient.put('/user/edit', userInfo);
}
