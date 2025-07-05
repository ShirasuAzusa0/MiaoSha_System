import type { PublicKeyResponse } from '@/types/auth'
import apiClient from '@/utils/apiClient'
import JSEncrypt from 'jsencrypt'

// 登录
export const login = async (account: string, password: string) => {
  try {
    const publicKeyResponse = await getPublicKey()
    const publicKey = publicKeyResponse.public_key

    const encrypt = new JSEncrypt()
    encrypt.setPublicKey(publicKey)
    const encryptedPassword = encrypt.encrypt(password)

    const formData = new FormData()
    formData.append('username', account)
    formData.append('password', encryptedPassword as string)

    return apiClient.post('/api/auth/login', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  } catch (error) {
    console.error('Login encryption or request failed:', error)
    throw error
  }
}

// 注册
export const signUp = async (username: string, account: string, password: string) => {
  const publicKeyResponse = await getPublicKey()
  const publicKey = publicKeyResponse.public_key

  const encrypt = new JSEncrypt()
  encrypt.setPublicKey(publicKey)
  const encryptedPassword = encrypt.encrypt(password)

  const formData = new FormData()
  formData.append('username', username)
  formData.append('account', account)
  formData.append('password', encryptedPassword as string)

  return apiClient.post('/api/auth/signup', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

// 公钥获取
export const getPublicKey = (): Promise<PublicKeyResponse> => {
  return apiClient.get('/key')
}
