<template>
<div class="logout-door-container">
  <div
    class="door-button"
    :class="{ opened: isHovered, closed: isClosing }"
    @mouseover="isHovered = true"
    @mouseleave="isHovered = false"
    @click="handleLogoutClick"
  >
    <div class="handle"></div>
  </div>
</div>

<div class="username-footer">
  👤 {{ authStore.currentUser?.username || 'Guest' }}
</div>

</template>



<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useToastStore } from '../stores/toast'

const router = useRouter()
const authStore = useAuthStore()
const toastStore = useToastStore()

const isHovered = ref(false)
const isClosing = ref(false)

function handleLogoutClick() {
  isClosing.value = true
  isHovered.value = false

  setTimeout(() => {
    authStore.logout()
    toastStore.show('You have been logged out.')

    if (router.currentRoute.value.name === 'home' || router.currentRoute.value.path === '/') {
      router.go(0)
    } else {
      router.push({ name: 'login' })
    }
  }, 1000)
}
</script>





<style scoped>
.logout-door-container {
  position: absolute;
  top: 16px;
  right: 20px;
  z-index: 1000;
}

.door-button {
  width: 30px;
  height: 50px;
  background: rgb(106, 4, 153);
  border-radius: 2px;
  cursor: pointer;
  transform-origin: left center;
  transition: transform 0.5s ease;
  position: relative;
  top: 4px;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5);
  background: repeating-linear-gradient(
    90deg,
    #cc7fe1, #7f9be1)
}



.door-button.opened {
  transform: rotateY(-20deg);
}

.door-button.closed {
  transform: rotateY(90deg);
  transition: transform 1s ease;
}

/* Türgriff */
.handle {
  width: 6px; 
  height: 6px;
  background: gold;
  border-radius: 50%;
  position: absolute;
  right: 2px; 
  top: 45%;
  box-shadow: 1px 1px 2px rgba(0,0,0,0.5);
}
.username-footer {
  position: fixed;
  bottom: 35px;
  right: 20px;
  font-weight: bold;
  color: #392a6d;
  font-size: 0.9rem;
  background: repeating-linear-gradient(
    90deg,
    #e6b9f2, #a8bef6);
  padding: 7px 12px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  transition: transform 0.3s ease;
}

.username-footer:hover {
  transform: translateY(-2px);
}


</style>

