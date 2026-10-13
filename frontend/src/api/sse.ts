export interface SseParser {
  push(chunk: string): string[]
  finish(): string[]
}

class IncrementalSseParser implements SseParser {
  private buffer = ''
  private dataLines: string[] = []

  push(chunk: string): string[] {
    this.buffer += chunk
    return this.drainLines(false)
  }

  finish(): string[] {
    const events = this.drainLines(true)

    if (this.buffer.length > 0) {
      this.processLine(this.buffer, events)
      this.buffer = ''
    }

    this.dispatch(events)
    return events
  }

  private drainLines(flushTrailingCarriageReturn: boolean): string[] {
    const events: string[] = []

    while (true) {
      const lineEnding = this.findLineEnding(flushTrailingCarriageReturn)
      if (!lineEnding) {
        break
      }

      const line = this.buffer.slice(0, lineEnding.index)
      this.buffer = this.buffer.slice(lineEnding.index + lineEnding.length)
      this.processLine(line, events)
    }

    return events
  }

  private findLineEnding(flushTrailingCarriageReturn: boolean): {
    index: number
    length: number
  } | null {
    for (let index = 0; index < this.buffer.length; index += 1) {
      const character = this.buffer[index]

      if (character === '\n') {
        return { index, length: 1 }
      }

      if (character === '\r') {
        if (index + 1 < this.buffer.length) {
          return {
            index,
            length: this.buffer[index + 1] === '\n' ? 2 : 1,
          }
        }

        return flushTrailingCarriageReturn ? { index, length: 1 } : null
      }
    }

    return null
  }

  private processLine(line: string, events: string[]): void {
    if (line === '') {
      this.dispatch(events)
      return
    }

    if (line.startsWith(':')) {
      return
    }

    const separatorIndex = line.indexOf(':')
    const field = separatorIndex === -1 ? line : line.slice(0, separatorIndex)
    const value = separatorIndex === -1 ? '' : line.slice(separatorIndex + 1)

    if (field === 'data') {
      this.dataLines.push(value)
    }
  }

  private dispatch(events: string[]): void {
    if (this.dataLines.length === 0) {
      return
    }

    events.push(this.dataLines.join('\n'))
    this.dataLines = []
  }
}

export function createSseParser(): SseParser {
  return new IncrementalSseParser()
}
