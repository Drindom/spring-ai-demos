<script setup lang="ts">
import { nextTick, ref } from 'vue'
import { ArrowUp } from 'lucide-vue-next'

const props = defineProps<{
  disabled: boolean
}>()

const emit = defineEmits<{
  submit: [message: string]
}>()

const input = ref('')
const textarea = ref<HTMLTextAreaElement | null>(null)

function resizeTextarea(): void {
  const element = textarea.value
  if (!element) {
    return
  }

  element.style.height = '0px'
  element.style.height = `${Math.min(element.scrollHeight, 160)}px`
}

function submit(): void {
  if (props.disabled || input.value.trim().length === 0) {
    return
  }

  emit('submit', input.value)
  input.value = ''

  void nextTick(() => {
    resizeTextarea()
    textarea.value?.focus()
  })
}

function handleKeydown(event: KeyboardEvent): void {
  if (event.key !== 'Enter' || event.shiftKey || event.isComposing) {
    return
  }

  event.preventDefault()
  submit()
}
</script>

<template>
  <div class="composer">
    <div class="composer__field" :class="{ 'composer__field--disabled': disabled }">
      <textarea
        ref="textarea"
        v-model="input"
        rows="1"
        maxlength="4000"
        placeholder="输入消息..."
        aria-label="输入消息"
        :disabled="disabled"
        @input="resizeTextarea"
        @keydown="handleKeydown"
      ></textarea>

      <button
        type="button"
        class="composer__send"
        :disabled="disabled || input.trim().length === 0"
        :aria-label="disabled ? '正在生成回复' : '发送消息'"
        :title="disabled ? '正在生成回复' : '发送消息'"
        @click="submit"
      >
        <ArrowUp :size="19" :stroke-width="2.2" aria-hidden="true" />
      </button>
    </div>

    <p class="composer__hint">Enter 发送 · Shift + Enter 换行</p>
  </div>
</template>

<style scoped>
.composer {
  width: 100%;
  max-width: 780px;
  margin: 0 auto;
  padding: 16px 24px 18px;
  box-sizing: border-box;
}

.composer__field {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  padding: 10px 10px 10px 15px;
  border: 1px solid var(--border-strong);
  border-radius: 8px;
  background: var(--surface);
  box-shadow: 0 5px 18px rgb(20 31 45 / 7%);
  transition: border-color 160ms ease, box-shadow 160ms ease;
}

.composer__field:focus-within {
  border-color: var(--brand);
  box-shadow: 0 0 0 3px var(--brand-focus), 0 5px 18px rgb(20 31 45 / 7%);
}

.composer__field--disabled {
  background: var(--surface-soft);
  box-shadow: none;
}

textarea {
  flex: 1;
  min-width: 0;
  min-height: 30px;
  max-height: 160px;
  padding: 4px 0;
  border: 0;
  outline: 0;
  resize: none;
  overflow-y: auto;
  color: var(--text-primary);
  background: transparent;
  font: inherit;
  font-size: 15px;
  line-height: 1.5;
  letter-spacing: 0;
}

textarea::placeholder {
  color: var(--text-tertiary);
}

textarea:disabled {
  cursor: not-allowed;
}

.composer__send {
  display: grid;
  flex: 0 0 36px;
  place-items: center;
  width: 36px;
  height: 36px;
  padding: 0;
  border: 0;
  border-radius: 7px;
  color: white;
  background: var(--brand-strong);
  cursor: pointer;
  transition: background 160ms ease, transform 160ms ease;
}

.composer__send:hover:not(:disabled) {
  background: var(--brand);
}

.composer__send:active:not(:disabled) {
  transform: translateY(1px);
}

.composer__send:focus-visible {
  outline: 3px solid var(--brand-focus);
  outline-offset: 2px;
}

.composer__send:disabled {
  color: var(--text-tertiary);
  background: var(--surface-muted);
  cursor: not-allowed;
}

.composer__hint {
  margin: 8px 0 0;
  color: var(--text-tertiary);
  font-size: 11px;
  line-height: 1.4;
  text-align: center;
}

@media (max-width: 640px) {
  .composer {
    padding: 12px 12px max(12px, env(safe-area-inset-bottom));
  }

  .composer__hint {
    display: none;
  }
}
</style>
