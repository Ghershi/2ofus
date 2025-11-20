<template>
  <div class="toast-container">
    <transition-group name="toast" tag="div">
      <div v-for="msg in toastStore.messages" :key="msg.id" class="toast">
        <span class="heart">❤️</span> {{ msg.message }}
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { useToastStore } from "@/stores/toast";
const toastStore = useToastStore();
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  max-width: 320px;
  z-index: 999000;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.toast {
  position: relative;
  background: rgba(255, 233, 246, 0.95); /* zartes Rosé */
  color: #5a2a63;
  border-radius: 20px;
  padding: 16px 24px;
  box-shadow: 0 6px 20px rgba(240, 180, 255, 0.4);
  font-family: 'Inter', sans-serif;
  font-size: 1.1rem;
  line-height: 1.5;
  user-select: none;
  cursor: default;
  animation: toastPopIn 0.5s ease;
}

.toast::after {
  content: "";
  position: absolute;
  right: -12px;
  top: 20px;
  width: 0;
  height: 0;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-left: 12px solid rgba(255, 233, 246, 0.95); /* Sprechblasen-Dreieck */
  filter: drop-shadow(2px 2px 2px rgba(240, 180, 255, 0.2));
}

/* Herz Animation optional */
.toast .heart {
  animation: heartbeat 1.5s infinite;
  font-size: 18px;
  margin-left: 8px;
}

/* Animation für Erscheinen */
@keyframes toastPopIn {
  from {
    opacity: 0;
    transform: translateX(40%) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

/* Vue-like Transition Klassen */
.toast-enter-active, .toast-leave-active {
  transition: all 0.4s ease;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(40%);
}
.toast-enter-to {
  opacity: 1;
  transform: translateX(0);
}
.toast-leave-from {
  opacity: 1;
  transform: translateX(0);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(40%);
}

</style>