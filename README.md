# Spring AI Demos

基于 Spring AI 的多模块实践项目，覆盖对话、工具调用、向量存储、RAG、MCP 与模型评估六类核心能力。

## 技术栈

- Java 21
- Spring Boot 4.1.0
- Spring AI 2.0.0
- Maven（多模块）
- Redis（向量存储）
- Reactor（流式响应）

## 功能模块

| 模块 | 端口 | 说明 |
|---|---|---|
| spring-ai-chat | 8080 | 对话与流式输出，支持会话记忆与自定义系统提示词 |
| spring-ai-tools | — | 工具定义（日期时间工具），供对话模块调用 |
| spring-ai-vector-redis | — | 基于 Redis 向量索引实现文档的写入、删除与相似度检索 |
| spring-ai-rag | 8081 | 检索增强问答：文档入库、相似度检索、RAG 对话 |
| spring-ai-mcp | 8081 / 8082 | MCP 服务端（暴露工具）与客户端（调用工具） |
| spring-ai-evaluation | 8084 | 回答相关性评估，输出通过与否、分数与反馈 |

## 环境要求

- JDK 21+
- Maven 3.9+
- Redis 7+（需支持向量索引）
- OpenAI 兼容的模型服务（对话模型与 Embedding 模型）

## 配置

各模块的 `application.yaml` 中配置模型地址与模型名，API Key 通过环境变量注入，不写入配置文件：

```bash
export DEEPSEEK_API_KEY=your_api_key
```

向量存储相关配置（模块：spring-ai-rag）：

```yaml
spring:
  ai:
    vectorstore:
      type: redis
      redis:
        initialize-schema: true
        index-name: spring-ai-rag-index
        prefix: "spring-ai:rag:"
  data:
    redis:
      host: localhost
      port: 6379
```

## 构建

```bash
mvn clean install
```

## 运行

各模块独立启动，在对应模块目录下执行：

```bash
cd spring-ai-chat
mvn spring-boot:run
```

## 接口说明

### 对话（spring-ai-chat，8080）

```
GET /chat?conversationId={id}&input={message}
```

以 SSE 流式返回模型输出。

### 文档入库（spring-ai-rag，8081）

```
POST /api/rag/documents/ingest
```

读取 classpath 下的文档，经分块与向量化后写入向量库，返回源文档数与分块数。

响应示例：

```json
{
  "sourceDocumentCount": 1,
  "chunkCount": 12
}
```

### 相似度检索（spring-ai-rag，8081）

```
POST /api/rag/retrieval/search
Content-Type: application/json

{
  "query": "什么是 RAG",
  "topK": 5
}
```

`topK` 取值范围 1 ~ 20。

### RAG 对话（spring-ai-rag，8081）

```
GET /chat?conversationId={id}&input={message}
```

以 SSE 流式返回，回答基于向量库检索结果生成。

### MCP 对话（mcp-client，8081）

```
GET /chat?conversationId={id}&input={message}
```

以 SSE 流式返回，模型可调用 MCP 服务端暴露的工具。

MCP 服务端（8082）：

```
POST /mcp
```

### 回答相关性评估（spring-ai-evaluation，8084）

```
GET /evaluation/relevancy
Content-Type: application/json

{
  "question": "什么是 RAG",
  "context": "RAG 是检索增强生成……"
}
```

响应示例：

```json
{
  "answer": "……",
  "pass": true,
  "score": 0.9,
  "feedback": "……"
}
```

## 目录结构

```
springai/
├── pom.xml
├── spring-ai-chat/            # 对话与流式输出
├── spring-ai-tools/           # 工具定义
├── spring-ai-vector-redis/    # 向量存储与检索
├── spring-ai-rag/             # 检索增强问答
├── spring-ai-mcp/
│   ├── mcp-client/            # MCP 客户端
│   └── mcp-server/            # MCP 服务端
└── spring-ai-evaluation/      # 回答评估
```

## 注意事项

- `spring-ai-rag` 与 `mcp-client` 默认端口均为 8081，同时启动会端口冲突，`spring-ai-rag` 需先执行文档入库接口，否则对话无检索结果。
- 各模块的模型地址默认指向本地模型服务，使用云端模型需修改 `application.yaml` 中的 `base-url` 与 `model`。
- 首次启动 `spring-ai-rag` 与 `spring-ai-vector-redis` 时会自动在 Redis 中创建向量索引。
