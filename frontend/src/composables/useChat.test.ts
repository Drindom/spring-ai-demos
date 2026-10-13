import { describe, expect, it } from 'vitest'
import type { ChatStreamClient } from '../api/chatStream'
import { useChat } from './useChat'

function createIds(): () => string {
  let index = 0
  return () => `message-${index++}`
}

describe('useChat', () => {
  it('adds a user message and appends assistant text deltas', async () => {
    const client: ChatStreamClient = async function* (message, options) {
      expect(message).toBe('你好')
      expect(options.conversationId).toBe('conversation-1')
      yield '你'
      yield '好'
    }
    const chat = useChat(client, createIds(), () => 'conversation-1')

    await expect(chat.sendMessage('  你好  ')).resolves.toBe(true)

    expect(chat.messages.value).toEqual([
      {
        id: 'message-0',
        role: 'user',
        content: '你好',
        status: 'complete',
      },
      {
        id: 'message-1',
        role: 'assistant',
        content: '你好',
        reasoning: '',
        status: 'complete',
      },
    ])
    expect(chat.isStreaming.value).toBe(false)
  })

  it('rejects empty input and a second request while streaming', async () => {
    let release: (() => void) | undefined
    const gate = new Promise<void>((resolve) => {
      release = resolve
    })
    const client: ChatStreamClient = async function* () {
      await gate
      yield 'done'
    }
    const chat = useChat(client, createIds())

    await expect(chat.sendMessage('   ')).resolves.toBe(false)
    const firstRequest = chat.sendMessage('first')

    expect(chat.isStreaming.value).toBe(true)
    await expect(chat.sendMessage('second')).resolves.toBe(false)
    expect(chat.messages.value).toHaveLength(2)

    release?.()
    await firstRequest
  })

  it('marks an interrupted partial response as an error', async () => {
    const client: ChatStreamClient = async function* () {
      yield 'partial'
      throw new Error('network disconnected')
    }
    const chat = useChat(client, createIds())

    await chat.sendMessage('hello')

    expect(chat.messages.value[1]).toMatchObject({
      content: 'partial',
      status: 'error',
    })
    expect(chat.isStreaming.value).toBe(false)
  })

  it('keeps message content empty when a request fails before content arrives', async () => {
    const client: ChatStreamClient = async function* () {
      throw new Error('backend unavailable')
    }
    const chat = useChat(client, createIds())

    await chat.sendMessage('hello')

    expect(chat.messages.value[1]).toMatchObject({
      reasoning: '',
      content: '',
      status: 'error',
    })
  })
})
