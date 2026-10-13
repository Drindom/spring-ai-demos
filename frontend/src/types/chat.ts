export type ChatRole = 'user' | 'assistant'

export type MessageStatus = 'pending' | 'streaming' | 'complete' | 'error'

export interface ChatMessage {
  id: string
  role: ChatRole
  content: string
  reasoning?: string
  status: MessageStatus
}
