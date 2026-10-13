// @vitest-environment happy-dom

import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import ChatComposer from './ChatComposer.vue'

describe('ChatComposer', () => {
  it('submits with Enter and clears the input', async () => {
    const wrapper = mount(ChatComposer, {
      props: { disabled: false },
    })
    const textarea = wrapper.get('textarea')

    await textarea.setValue('hello')
    await textarea.trigger('keydown', {
      key: 'Enter',
      shiftKey: false,
      isComposing: false,
    })

    expect(wrapper.emitted('submit')).toEqual([['hello']])
    expect((textarea.element as HTMLTextAreaElement).value).toBe('')
  })

  it('keeps the draft and does not submit with Shift+Enter', async () => {
    const wrapper = mount(ChatComposer, {
      props: { disabled: false },
    })
    const textarea = wrapper.get('textarea')

    await textarea.setValue('first line')
    await textarea.trigger('keydown', {
      key: 'Enter',
      shiftKey: true,
      isComposing: false,
    })

    expect(wrapper.emitted('submit')).toBeUndefined()
    expect((textarea.element as HTMLTextAreaElement).value).toBe('first line')
  })

  it('does not submit while an input method is composing text', async () => {
    const wrapper = mount(ChatComposer, {
      props: { disabled: false },
    })
    const textarea = wrapper.get('textarea')

    await textarea.setValue('拼音输入')
    await textarea.trigger('keydown', {
      key: 'Enter',
      shiftKey: false,
      isComposing: true,
    })

    expect(wrapper.emitted('submit')).toBeUndefined()
  })

  it('disables both input and submission while streaming', async () => {
    const wrapper = mount(ChatComposer, {
      props: { disabled: true },
    })

    expect(wrapper.get('textarea').attributes('disabled')).toBeDefined()
    expect(wrapper.get('button').attributes('disabled')).toBeDefined()
    expect(wrapper.emitted('submit')).toBeUndefined()
  })
})
