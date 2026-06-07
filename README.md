# MinimalHealth Pro

极简健康管理 App — Vue 3 前端 + Spring Boot 后端 + Capacitor Android 打包

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | Vue 3 + TypeScript + Vite |
| 后端 | Spring Boot 3 + JPA + H2 |
| 移动端 | Capacitor 7 (Android APK) |
| 数据库 | H2 (文件模式) |

## 项目结构

```
MinimalHealth/
├── minimalhealth-pro/       # 前端 (Vue 3)
│   ├── src/views/           # 17 个页面
│   ├── src/api/             # API 调用层
│   └── android/             # Capacitor Android 项目
├── minimalhealth-server/    # 后端 (Spring Boot)
│   └── src/main/java/com/minimalhealth/
└── MinimalHealth.md         # 产品设计文档
```

## 快速启动

### 后端
```bash
cd minimalhealth-server
mvn spring-boot:run
# 运行在 http://localhost:8080
```

### 前端 (开发)
```bash
cd minimalhealth-pro
npm install
npm run dev
# 运行在 http://localhost:5173
```

### Android APK
```bash
cd minimalhealth-pro
npm run build
npx cap sync android
cd android && ./gradlew assembleDebug
# APK 在 android/app/build/outputs/apk/debug/
```

## 环境变量

| 变量 | 说明 | 默认值 |
|---|---|---|
| `JWT_SECRET` | JWT 签名密钥 | 内置 dev 占位值 |
| `DB_PASSWORD` | 数据库密码 | (H2 无需) |
