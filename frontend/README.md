# AI 对话前端

基于 Vue 3、Vite 和 TypeScript 的流式对话窗口，当前接入后端 `GET /chat?conversationId=...&input=...` 接口。

## 本地运行

环境要求：Node.js 22+、npm 10+，后端使用 Java 21 和 Maven。

1. 配置后端所需的 `DEEPSEEK_API_KEY` 环境变量。
2. 在 `spring-ai-chat` 目录启动 Spring Boot，服务监听 `http://localhost:8081`。
3. 安装前端依赖并启动开发服务器：

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 Vite 输出的本地地址，默认通常是 `http://localhost:5173`。

开发服务器会将 `/chat` 请求代理到 `http://localhost:8081`，因此无需修改后端 CORS。该代理只在 Vite 开发服务器中生效；生产环境需要配置同源反向代理或单独设置 API 地址。

## 可用命令

```bash
npm run type-check
npm test -- --run
npm run build
```

当前对话记录仅保存在页面内存中。页面初始化时会随机创建一个会话 ID，同一页面内的请求复用该 ID；刷新页面后开始新会话。
