import { createSseParser } from './sse'

export interface StreamChatOptions {
  conversationId: string
  signal?: AbortSignal
}

export type ChatStreamClient = (
  message: string,
  options: StreamChatOptions,
) => AsyncIterable<string>

export class ChatStreamError extends Error {
  readonly status?: number

  constructor(message: string, status?: number) {
    super(message)
    this.name = 'ChatStreamError'
    this.status = status
  }
}

export async function* streamChat(
  message: string,
  options: StreamChatOptions,
): AsyncGenerator<string> {
  const query = new URLSearchParams({
    conversationId: options.conversationId,
    input: message,
  })
  const response = await fetch(`/chat?${query.toString()}`, {
    headers: {
      Accept: 'text/event-stream',
    },
    signal: options.signal,
  })

  if (!response.ok) {
    throw new ChatStreamError(`请求失败，状态码 ${response.status}`, response.status)
  }

  const contentType = response.headers.get('content-type')
  if (!contentType?.toLowerCase().includes('text/event-stream')) {
    throw new ChatStreamError('服务端返回了无法识别的响应格式')
  }

  if (!response.body) {
    throw new ChatStreamError('浏览器未收到可读取的响应流')
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder()
  const parser = createSseParser()

  function* parseEvents(events: string[]): Generator<string> {
    for (const event of events) {
      if (event.length > 0) {
        yield event
      }
    }
  }

  try {
    while (true) {
      const { done, value } = await reader.read()
      if (done) {
        break
      }

      yield* parseEvents(parser.push(decoder.decode(value, { stream: true })))
    }

    const decoderRemainder = decoder.decode()
    if (decoderRemainder.length > 0) {
      yield* parseEvents(parser.push(decoderRemainder))
    }

    yield* parseEvents(parser.finish())
  } finally {
    reader.releaseLock()
  }
}
