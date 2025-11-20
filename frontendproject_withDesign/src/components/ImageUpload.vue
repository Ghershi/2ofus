<!-- components/ImageUpload.vue -->
<template>
  <div class="my-4">
    <input type="file" accept="image/*" multiple @change="handleUpload" />
  </div>
</template>

<script setup>
const emit = defineEmits(['images-uploaded'])

function handleUpload(event) {
  const files = Array.from(event.target.files)
  const urls = []

  files.forEach(file => {
    const reader = new FileReader()
    reader.onload = (e) => {
      urls.push(e.target.result)
      if (urls.length === files.length) {
        emit('images-uploaded', urls)
      }
    }
    reader.readAsDataURL(file)
  })
}
</script>