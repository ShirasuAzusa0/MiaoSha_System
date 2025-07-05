项目架构如下：
src/main/java/.../
├─ controller           控制层，只处理HTTP请求和响应
├─ service              服务层，业务逻辑（数据库操作）
├─ repository           仓库层，与数据库打交道的接口
├─ entity               实体层，与表结构一一映射的实体类
│     ├─ dto            数据交换层，用于系统内部/系统之间传输数据（Controller和Service之间、Service和外部系统之间）
│     ├─ vo             视图层，向前端返回数据，仅负责数据展示
│     └─ filter         自定义过滤器类
│
├─ mapper               映射类的映射代码实现层
├─ key                  RSA非对称加密算法的密钥
├─ util                 各种通用工具类方法
├─ task                 定时层，定时任务类
└─ xxxApplication       启动类
