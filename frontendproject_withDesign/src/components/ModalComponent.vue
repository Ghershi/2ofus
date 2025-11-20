<template>
  <div class="modal">
    <button class="close-btn" @click="closeModal" aria-label="Schließen">
      ×
    </button>
    <slot></slot>
  </div>
</template>
ç

<script setup>
  const emit = defineEmits(["close"]);

  function closeModal() {
    emit("close");
  }
</script>

<style scoped>
  .modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    max-width: 80%;
    max-height: 90%;
    border-radius: 20px;
    box-sizing: border-box;
    text-align: center;
    z-index: 2;
    overflow-y: auto;
  }

  .close-btn {
    position: absolute;
    top: 12px;
    right: 17px;
    background: transparent;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    color: #888;
    transition: color 0.2s;
    line-height: 1;
    padding: 0;
  }
  .close-btn:hover {
    color: #333;
  }
</style>
