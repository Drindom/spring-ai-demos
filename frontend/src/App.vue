<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import { Bot, Radio } from 'lucide-vue-next'
import ChatComposer from './components/ChatComposer.vue'
import ChatMessage from './components/ChatMessage.vue'
import { useChat } from './composables/useChat'

const { messages, isStreaming, sendMessage } = useChat()
const messageList = ref<HTMLElement | null>(null)

async function scrollToLatest(): Promise<void> {
  await nextTick()
  const element = messageList.value
  if (element) {
    element.scrollTop = element.scrollHeight
  }
}

async function handleSubmit(message: string): Promise<void> {
  void sendMessage(message)
  await scrollToLatest()
}

watch(
  () =>
    messages.value.map(
      (message) =>
        `${message.id}:${message.reasoning?.length ?? 0}:${message.content.length}:${message.status}`,
    ),
  () => {
    void scrollToLatest()
  },
)
</script>

<template>
  <main class="chat-shell">
    <section class="chat-panel" aria-label="AI 对话窗口">
      <header class="chat-header">
        <div class="chat-header__identity">
          <span class="chat-header__mark" aria-hidden="true">
            <Bot :size="20" :stroke-width="1.9" />
          </span>
          <div>
            <h1>AI 对话</h1>
            <p>流式智能助手</p>
          </div>
        </div>

        <div class="chat-header__status" :class="{ 'chat-header__status--active': isStreaming }">
          <Radio :size="14" :stroke-width="2" aria-hidden="true" />
          <span>{{ isStreaming ? '生成中' : '已就绪' }}</span>
        </div>
      </header>

      <div ref="messageList" class="chat-messages" aria-live="polite">
        <div v-if="messages.length === 0" class="empty-state">
          <span class="empty-state__icon" aria-hidden="true">
            <Bot :size="28" :stroke-width="1.5" />
          </span>
          <h2>开始一段对话</h2>
          <p>发送一条消息，回复会在这里实时显示。</p>
        </div>

        <div v-else class="message-list">
          <ChatMessage v-for="message in messages" :key="message.id" :message="message" />
        </div>
      </div>

      <footer class="chat-footer">
        <ChatComposer :disabled="isStreaming" @submit="handleSubmit" />
      </footer>
    </section>
  </main>
</template>
