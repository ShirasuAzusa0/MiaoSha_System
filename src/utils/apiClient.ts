import axios from "axios";
import { ElMessage } from "element-plus";

// axios实例
const apiClient = axios.create({
  baseURL: 'http://127.0.0.1:8080',// TODO: 切换到环境变量
  // baseURL: 'http://127.0.0.1:4523/m1/6504726-6205188-default',
  timeout: 5000,
});

// 拦截器
// axios请求拦截器
apiClient.interceptors.request.use(
  // TODO: 拼接token
)

// axios响应式拦截器
apiClient.interceptors.response.use(
  // 成功的回调：直接返回 res.data
  (res) => res.data,
  // 失败的回调
  (e) => {
    let message = '发生未知错误';

    if (e.response) {
      // 根据不同的状态码进行处理
      switch (e.response.status) {
        case 400:
          // 优先使用后端返回的错误信息
          message = e.response.data.msg || '请求参数错误';
          break;
        case 401:
          message = '未授权，请重新登录';
          // 在这里可以执行跳转到登录页等操作
          break;
        case 403:
          message = '禁止访问';
          break;
        case 404:
          message = '请求资源不存在';
          break;
        default:
          message = e.response.data.msg || `服务器错误 (${e.response.status})`;
      }
    } else if (e.request) {
      // 2. 请求已发出，但没有收到响应（例如网络问题或超时）
      if (e.message.includes('timeout')) {
        message = '请求超时，请检查网络连接';
      } else {
        message = '网络错误，无法连接到服务器';
      }
    }

    // 统一显示错误提示
    ElMessage({
      type: 'error',
      message,
      plain: true
    });

    // 继续抛出错误，以便业务代码中的 .catch() 能捕获到
    return Promise.reject(e);
  },
)

export default apiClient;
