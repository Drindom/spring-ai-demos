import { afterEach, describe, expect, it, vi } from 'vitest'
import { streamChat } from './chatStream'

function createStream(chunks: Uint8Array[]): ReadableStream<Uint8Array> {
  return new ReadableStream({
    start(controller) {
      for (const chunk of chunks) {
        controller.enqueue(chunk)
      }
      controller.close()
    },
  })
}

async function collect(stream: AsyncIterable<string>): Promise<string[]> {
  const values: string[] = []
  for await (const value of stream) {
    values.push(value)
  }
  return values
}

afterEach(() => {
  vi.unstubAllGlobals()
})

describe('streamChat', () => {
  it('encodes the message and yields text SSE events', async () => {
    const encoder = new TextEncoder()
    const bytes = encoder.encode('data:你好\n\ndata: world\n\n')
    const fetchMock = vi.fn().mockResolvedValue(
      new Response(createStream([bytes.slice(0, 8), bytes.slice(8, 11), bytes.slice(11)]), {
        headers: { 'Content-Type': 'text/event-stream;charset=UTF-8' },
      }),
    )
    vi.stubGlobal('fetch', fetchMock)

    await expect(
      collect(streamChat('空 格 & symbol', { conversationId: 'conversation-1' })),
    ).resolves.toEqual(['你好', ' world'])
    expect(fetchMock).toHaveBeenCalledWith(
      '/chat?conversationId=conversation-1&input=%E7%A9%BA+%E6%A0%BC+%26+symbol',
      expect.objectContaining({
        headers: { Accept: 'text/event-stream' },
      }),
    )
  })

  it('throws a typed error for non-successful responses', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(new Response(null, { status: 502 })),
    )

    const result = collect(streamChat('hello', { conversationId: 'conversation-1' }))

    await expect(result).rejects.toMatchObject({
      name: 'ChatStreamError',
      status: 502,
    })
  })

  it('rejects responses that are not event streams', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        new Response('plain text', {
          headers: { 'Content-Type': 'text/plain' },
        }),
      ),
    )

    await expect(
      collect(streamChat('hello', { conversationId: 'conversation-1' })),
    ).rejects.toThrow('无法识别的响应格式')
  })

  it('flushes a text SSE event at clean EOF', async () => {
    const encoder = new TextEncoder()
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        new Response(createStream([encoder.encode('data:final')]), {
          headers: { 'Content-Type': 'text/event-stream' },
        }),
      ),
    )

    await expect(
      collect(streamChat('hello', { conversationId: 'conversation-1' })),
    ).resolves.toEqual(['final'])
  })
})
