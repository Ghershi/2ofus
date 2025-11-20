import { defineStore } from "pinia";
import { ref } from "vue";

export const useToastStore = defineStore("toastStore", () => {
  const messages = ref([]);

  function show(message, duration = 3000) {
    const id = Date.now() + Math.random();
    messages.value.push({ id, message });
    // remove after duration
    setTimeout(() => {
      remove(id);
    }, duration);
  }

  function add(message) {
    messages.value.push({
      id: message.id || Date.now(),
      message: message.message,
      type: message.type || 'default'
    });
  }

  function remove(id) {
    messages.value = messages.value.filter((m) => m.id !== id);
  }

  return {
    messages,
    show,
    add,
    remove,
  };
});
