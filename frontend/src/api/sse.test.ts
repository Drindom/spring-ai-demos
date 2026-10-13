import { describe, expect, it } from 'vitest'
import { createSseParser } from './sse'

describe('createSseParser', () => {
  it('parses an event split across arbitrary chunks', () => {
    const parser = createSseParser()

    expect(parser.push('da')).toEqual([])
    expect(parser.push('ta:你')).toEqual([])
    expect(parser.push('好\n')).toEqual([])
    expect(parser.push('\n')).toEqual(['你好'])
  })

  it('parses multiple events in one chunk', () => {
    const parser = createSseParser()

    expect(parser.push('data:first\n\ndata:second\n\n')).toEqual(['first', 'second'])
  })

  it('preserves leading spaces from Spring model chunks', () => {
    const parser = createSseParser()

    expect(parser.push('data:Here\n\ndata: are\n\ndata: five\n\n')).toEqual([
      'Here',
      ' are',
      ' five',
    ])
  })

  it('supports CRLF, comments, and multiline data fields', () => {
    const parser = createSseParser()

    expect(
      parser.push(': heartbeat\r\nevent: message\r\ndata:first line\r\ndata:second line\r\n\r\n'),
    ).toEqual(['first line\nsecond line'])
  })

  it('flushes a final event when the stream ends without a blank line', () => {
    const parser = createSseParser()

    expect(parser.push('data:tail')).toEqual([])
    expect(parser.finish()).toEqual(['tail'])
  })

  it('ignores frames without data fields', () => {
    const parser = createSseParser()

    expect(parser.push('id: 1\nretry: 2000\n\n')).toEqual([])
    expect(parser.finish()).toEqual([])
  })
})
