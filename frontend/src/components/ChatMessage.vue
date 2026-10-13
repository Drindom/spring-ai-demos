<script setup lang="ts">
import { Bot, BrainCircuit, ChevronDown, UserRound } from 'lucide-vue-next'
import type { ChatMessage as ChatMessageModel } from '../types/chat'

defineProps<{
  message: ChatMessageModel
}>()
</script>

<template>
  <article
    class="message"
    :class="`message--${message.role}`"
    :aria-label="message.role === 'assistant' ? 'AI 回复' : '你的消息'"
  >
    <div class="message__avatar" aria-hidden="true">
      <Bot v-if="message.role === 'assistant'" :size="18" :stroke-width="1.8" />
      <UserRound v-else :size="18" :stroke-width="1.8" />
    </div>

    <div class="message__body">
      <span class="message__author">
        {{ message.role === 'assistant' ? 'AI 助手' : '你' }}
      </span>

      <details
        v-if="message.role === 'assistant' && message.reasoning"
        class="message__reasoning"
        :open="message.status === 'streaming' && !message.content"
      >
        <summary>
          <BrainCircuit :size="15" :stroke-width="1.8" aria-hidden="true" />
          <span>{{ message.status === 'streaming' && !message.content ? '正在思考' : '思考过程' }}</span>
          <ChevronDown
            class="message__reasoning-chevron"
            :size="15"
            :stroke-width="2"
            aria-hidden="true"
          />
        </summary>
        <div class="message__reasoning-content">{{ message.reasoning }}</div>
      </details>

      <div v-if="message.content" class="message__content">
        {{ message.content }}
      </div>

      <div
        v-if="message.status === 'pending'"
        class="message__thinking"
        role="status"
        aria-label="AI 正在思考"
      >
        <span></span>
        <span></span>
        <span></span>
      </div>

      <span
        v-else-if="message.status === 'streaming' && (message.content || !message.reasoning)"
        class="message__cursor"
        aria-label="AI 正在回复"
      ></span>
    </div>
  </article>
</template>

<style scoped>
.message {
  display: grid;
  grid-template-columns: 36px minmax(0, 1fr);
  gap: 14px;
  width: 100%;
  padding: 22px 24px;
  box-sizing: border-box;
}

.message--assistant {
  background: var(--surface-soft);
}

.message__avatar {
  display: grid;
  place-items: center;
  width: 36px;
  height: 36px;
  border: 1px solid var(--border-strong);
  border-radius: 8px;
  color: var(--text-secondary);
  background: var(--surface);
}

.message--user .message__avatar {
  color: var(--brand-strong);
  background: var(--brand-soft);
  border-color: var(--brand-border);
}

.message__body {
  min-width: 0;
  padding-top: 1px;
}

.message__author {
  display: block;
  margin-bottom: 7px;
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 650;
  line-height: 1.3;
}

.message__reasoning {
  margin: 0 0 13px;
  padding-left: 12px;
  border-left: 2px solid var(--border-strong);
  color: var(--text-secondary);
}

.message__reasoning summary {
  display: flex;
  align-items: center;
  width: fit-content;
  max-width: 100%;
  gap: 6px;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  line-height: 24px;
  list-style: none;
  user-select: none;
}

.message__reasoning summary::-webkit-details-marker {
  display: none;
}

.message__reasoning summary:focus-visible {
  border-radius: 4px;
  outline: 3px solid var(--brand-focus);
  outline-offset: 2px;
}

.message__reasoning-chevron {
  flex: 0 0 auto;
  transition: transform 160ms ease;
}

.message__reasoning[open] .message__reasoning-chevron {
  transform: rotate(180deg);
}

.message__reasoning-content {
  margin-top: 7px;
  color: var(--text-secondary);
  font-size: 13px;
  line-height: 1.7;
  overflow-wrap: anywhere;
  white-space: pre-wrap;
}

.message__content {
  color: var(--text-primary);
  font-size: 15px;
  line-height: 1.75;
  overflow-wrap: anywhere;
  white-space: pre-wrap;
}

.message__thinking {
  display: flex;
  align-items: center;
  gap: 4px;
  width: 36px;
  height: 25px;
}

.message__thinking span {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--text-tertiary);
  animation: thinking 1.15s ease-in-out infinite;
}

.message__thinking span:nth-child(2) {
  animation-delay: 0.16s;
}

.message__thinking span:nth-child(3) {
  animation-delay: 0.32s;
}

.message__cursor {
  display: inline-block;
  width: 2px;
  height: 16px;
  margin-left: 2px;
  vertical-align: -2px;
  background: var(--brand);
  animation: blink 0.9s step-end infinite;
}

@keyframes thinking {
  0%,
  60%,
  100% {
    opacity: 0.35;
    transform: translateY(0);
  }

  30% {
    opacity: 1;
    transform: translateY(-3px);
  }
}

@keyframes blink {
  0%,
  45% {
    opacity: 1;
  }

  46%,
  100% {
    opacity: 0;
  }
}

@media (max-width: 640px) {
  .message {
    grid-template-columns: 32px minmax(0, 1fr);
    gap: 11px;
    padding: 18px 16px;
  }

  .message__avatar {
    width: 32px;
    height: 32px;
  }

  .message__content {
    font-size: 14px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .message__thinking span,
  .message__cursor {
    animation: none;
  }

  .message__reasoning-chevron {
    transition: none;
  }
}
</style>
