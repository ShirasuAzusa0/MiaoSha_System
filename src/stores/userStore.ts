import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import type { UserInfo } from '@/types/userInfo'
import { login as apiLogin, signUp as apiSignUp } from '@/apis/authApi'
import { getUserInfo as apiGetUserInfo, updateUserInfo as apiUpdateUserInfo } from '@/apis/userApi'
import type { CartItem } from '@/types/goods'


export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(JSON.parse(localStorage.getItem('user') || 'null'))
  const isLoggedIn = ref(JSON.parse(localStorage.getItem('isLoggedIn') || 'false'))
  const username = ref(localStorage.getItem('username') || '')
  const token = ref(localStorage.getItem('token') || '')

  // Cart state
  const cartItems = ref<CartItem[]>(JSON.parse(localStorage.getItem('cartItems') || '[]'))

  // Watch for changes in user data and save to localStorage
  watch(
    user,
    (newUser) => {
      localStorage.setItem('user', JSON.stringify(newUser))
    },
    { deep: true },
  )

  watch(isLoggedIn, (newStatus) => {
    localStorage.setItem('isLoggedIn', JSON.stringify(newStatus))
  })

  watch(username, (newName) => {
    localStorage.setItem('username', newName)
  })

  watch(token, (newToken) => {
    localStorage.setItem('token', newToken)
  })

  // Watch for changes in cartItems and save to localStorage
  watch(
    cartItems,
    (newCartItems) => {
      localStorage.setItem('cartItems', JSON.stringify(newCartItems))
    },
    { deep: true },
  )

  const login = async (account: string, password: string) => {
    try {
      const response = await apiLogin(account, password)
      if (response.data && response.data.userId) {
        await fetchUserInfo(response.data.userId)
      } else if (response.data) {
        user.value = response.data as UserInfo
      }
      isLoggedIn.value = true
      username.value = account
      token.value = response.data.token // Assuming the token is returned in the response
      return response
    } catch (error) {
      console.error('Login failed:', error)
      throw error
    }
  }

  const logout = () => {
    user.value = null
    isLoggedIn.value = false
    username.value = ''
    token.value = ''
    cartItems.value = [] // Clear cart on logout
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('username')
    localStorage.removeItem('token')
    localStorage.removeItem('user') // Clear user data from local storage
    localStorage.removeItem('cartItems') // Clear cart from local storage
  }

  const fetchUserInfo = async (userId: string) => {
    try {
      const response = await apiGetUserInfo(userId)
      if (response.data) {
        user.value = response.data as UserInfo
      }
      return response
    } catch (error) {
      console.error('Failed to fetch user info:', error)
      throw error
    }
  }

  const editUserInfo = async (userInfo: UserInfo) => {
    try {
      const response = await apiUpdateUserInfo(userInfo)
      if (response.data) {
        user.value = response.data as UserInfo
      }
      return response
    } catch (error) {
      console.error('Failed to update user info:', error)
      throw error
    }
  }

  // signUp action - not directly affecting store user state immediately, but useful to include
  const signUp = async (username: string, account: string, password: string) => {
    try {
      const response = await apiSignUp(username, account, password)
      return response
    } catch (error) {
      console.error('Sign up failed:', error)
      throw error
    }
  }

  function addCartItem(item: CartItem) {
    const existingItem = cartItems.value.find((cartItem) => cartItem.goodId === item.goodId)
    if (existingItem) {
      existingItem.quantity += item.quantity
    } else {
      cartItems.value.push(item)
    }
  }

  function updateCartItemQuantity(goodId: number, quantity: number) {
    const item = cartItems.value.find((cartItem) => cartItem.goodId === goodId)
    if (item) {
      if (quantity > 0) {
        item.quantity = quantity
      } else {
        // Optionally remove if quantity is 0 or less
        removeCartItem(goodId)
      }
    }
  }

  function removeCartItem(goodId: number) {
    cartItems.value = cartItems.value.filter((item) => item.goodId !== goodId)
  }

  function clearCart() {
    cartItems.value = []
  }

  return {
    user,
    isLoggedIn,
    username,
    token,
    cartItems,
    login,
    logout,
    fetchUserInfo,
    editUserInfo,
    signUp,
    addCartItem,
    updateCartItemQuantity,
    removeCartItem,
    clearCart,
  }
})
