import { computed, ref } from 'vue'
import { streamChat, type ChatStreamClient } from '../api/chatStream'
import type { ChatMessage } from '../types/chat'

function createMessageId(): string {
  if (typeof crypto.randomUUID === 'function') {
    return crypto.randomUUID()
  }

  const bytes = crypto.getRandomValues(new Uint8Array(16))
  bytes[6] = (bytes[6]! & 0x0f) | 0x40
  bytes[8] = (bytes[8]! & 0x3f) | 0x80

  const hex = Array.from(bytes, (byte) => byte.toString(16).padStart(2, '0')).join('')
  return `${hex.slice(0, 8)}-${hex.slice(8, 12)}-${hex.slice(12, 16)}-${hex.slice(16, 20)}-${hex.slice(20)}`
}

export function useChat(
  client: ChatStreamClient = streamChat,
  createId: () => string = createMessageId,
  createConversationId: () => string = createMessageId,
) {
  const conversationId = createConversationId()
  const messages = ref<ChatMessage[]>([])
  const isStreaming = ref(false)

  const canSend = computed(() => !isStreaming.value)

  async function sendMessage(input: string): Promise<boolean> {
    const content = input.trim()
    if (content.length === 0 || isStreaming.value) {
      return false
    }

    const assistantMessageIndex = messages.value.length
    messages.value.push(
      {
        id: createId(),
        role: 'user',
        content,
        status: 'complete',
      },
      {
        id: createId(),
        role: 'assistant',
        content: '',
        reasoning: '',
        status: 'pending',
      },
    )
    isStreaming.value = true

    try {
      for await (const delta of client(content, { conversationId })) {
        const assistantMessage = messages.value[assistantMessageIndex + 1]
        if (!assistantMessage) {
          continue
        }

        assistantMessage.status = 'streaming'
        assistantMessage.content += delta
      }

      const assistantMessage = messages.value[assistantMessageIndex + 1]
      if (assistantMessage) {
        assistantMessage.status = 'complete'
      }
    } catch {
      const assistantMessage = messages.value[assistantMessageIndex + 1]
      if (assistantMessage) {
        assistantMessage.status = 'error'
      }
    } finally {
      isStreaming.value = false
    }

    return true
  }

  return {
    messages,
    isStreaming,
    canSend,
    sendMessage,
  }
}
