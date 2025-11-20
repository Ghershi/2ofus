<template>
  <NavbarLogout></NavbarLogout>
  <div class="animated-background">
    <!-- Gradient-Füllung für Wolken -->
    <svg width="0" height="0">
      <defs>
        <linearGradient id="cloudGradient" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stop-color="#ffffff" />
          <stop offset="100%" stop-color="#e0dfff" />
        </linearGradient>
      </defs>
    </svg>

    <!-- SVG Wolken -->
    <svg class="cloud-svg cloud-1" viewBox="0 0 64 32">
      <path
        d="M20,30 C10,30 10,15 20,15 C22,10 28,10 30,15 C40,15 40,30 30,30 Z"
      />
    </svg>
    <svg class="cloud-svg cloud-2" viewBox="0 0 64 32">
      <path d="M10,25 C5,25 5,10 15,10 C17,5 27,5 30,10 C45,10 45,25 30,25 Z" />
    </svg>
    <svg class="cloud-svg cloud-3" viewBox="0 0 64 32">
      <path d="M15,28 C8,28 8,13 18,13 C20,8 30,8 32,13 C44,13 44,28 32,28 Z" />
    </svg>
    <svg class="cloud-svg cloud-4" viewBox="0 0 64 32">
      <path
        d="M18,26 C12,26 12,12 20,12 C22,6 30,6 32,12 C44,12 44,26 32,26 Z"
      />
    </svg>
    <svg class="cloud-svg cloud-5" viewBox="0 0 64 32">
      <path
        d="M22,29 C14,29 14,14 22,14 C25,9 33,9 35,14 C48,14 48,29 35,29 Z"
      />
    </svg>
    <svg class="cloud-svg cloud-6" viewBox="0 0 64 32">
      <path
        d="M16,27 C10,27 10,13 18,13 C20,9 28,9 30,13 C42,13 42,27 30,27 Z"
      />
    </svg>

    <!-- Sterne -->
    <div
      class="star"
      v-for="n in 40"
      :key="n"
      :style="randomStarStyle(n)"
    ></div>

    <!-- Mond -->
    <div class="moon"></div>
  </div>

  <!-- Logo -->
  <img src="@/assets/2OfUsKopie.png" alt="Logo" class="logo" />

  <!-- Chat UI -->
  <div class="chat-container">
    <RouterLink
      to="/connection"
      class="close-button"
      title="Close Chat"
      @click="saveVisitTime"
      >×</RouterLink
    >

    <div v-for="(message, index) in messages" :key="index" class="message">
      <img
        v-if="message.image"
        :src="message.image"
        class="chat-image"
        @click="openImage(message.image)"
      />
    </div>

    <div class="entries" ref="entryContainer" :class="{ blurred: isLoading }">
      <div
        class="entry-card"
        :class="
          entry.senderEmail === currentUserEmail ? 'own-entry' : 'other-entry'
        "
        v-for="(entry, index) in journalEntries"
        :key="index"
      >
        <div class="entry-content">
          <p v-if="entry.text">{{ entry.text }}</p>
          <img v-if="entry.imageUrl" :src="entry.imageUrl" alt="Bild" />
        </div>
        <small class="timestamp">{{ formatDate(entry.timestamp) }}</small>
      </div>
    </div>

    <form class="chat-form" @submit.prevent="submitEntry">
      <textarea v-model="text" placeholder="Write a message..." required />
      <label class="upload-btn" v-if="!isUploading">
        📷
        <input type="file" accept="image/*" @change="onFileChange" hidden />
      </label>
      <div class="spinner" v-else></div>
      <button type="submit">➤</button>
    </form>
  </div>

  <div v-if="isLoading" class="loading-overlay">
    <img src="@/assets/2OfUsKopie.png" class="loading-logo" />
  </div>
</template>

<script setup>
  import { ref, onMounted, nextTick, computed, watch } from "vue";
  import { useAuthStore } from "@/stores/auth.js";
  import { useJournalStore } from "@/stores/journal";
  import NavbarLogout from "@/components/NavbarLogout.vue";

  const currentUser = ref("");
  const currentUserEmail = computed(
    () => authStore.currentUser?.userEmail || ""
  );
  const text = ref("");
  const journalEntries = ref([]);
  const entryContainer = ref(null);
  const isUploading = ref(false);

  const journalStore = useJournalStore();
  const authStore = useAuthStore();

  const scrollToBottom = () => {
    nextTick(() => {
      const container = entryContainer.value;
      const images = container?.querySelectorAll("img") || [];

      const imagePromises = Array.from(images).map(
        (img) =>
          new Promise((resolve) => {
            if (img.complete) {
              resolve();
            } else {
              img.onload = resolve;
              img.onerror = resolve;
            }
          })
      );

      Promise.all(imagePromises).then(() => {
        container?.scrollTo({
          top: container.scrollHeight,
          behavior: "smooth", // <--- sanft scrollen
        });
      });
    });
  };

  const formatDate = (isoString) => {
    const d = new Date(isoString);
    return d.toLocaleString(); // z.B. "11.7.2025, 14:32"
  };

  const fetchEntries = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/journal", {
        headers: {
          ...authStore.authHeader,
        },
      });
      if (!res.ok) throw new Error("Messages could not be loaded");
      const data = await res.json();
      journalEntries.value = data;
    } catch (err) {
      console.error(err);
    }
  };
  const submitEntry = async () => {
    if (!text.value.trim()) return;

    const dto = {
      text: text.value,
      senderEmail: currentUser.value,
    };

    const formData = new FormData();
    formData.append("dto", JSON.stringify(dto));

    // Hier kein Bild, also nichts anhängen

    text.value = "";

    const res = await fetch("http://localhost:8080/api/journal", {
      method: "POST",
      headers: {
        Authorization: authStore.authHeader?.Authorization || "",
      },
      body: formData,
    });

    if (res.ok) {
      await fetchEntries();
      scrollToBottom();
    } else {
      const err = await res.text();
      alert("Fehler beim Senden: " + err);
    }
  };
  const onFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const dto = {
      text: "", // falls leer
      senderEmail: currentUser.value,
    };

    const formData = new FormData();
    formData.append("dto", JSON.stringify(dto));
    formData.append("image", file);

    const res = await fetch("http://localhost:8080/api/journal", {
      method: "POST",
      headers: {
        Authorization: authStore.authHeader?.Authorization || "",
      },
      body: formData,
    });

    if (res.ok) {
      await fetchEntries();
      scrollToBottom();
    } else {
      const err = await res.text();
      alert("Fehler beim Senden: " + err);
    }
  };

  const isLoading = ref(true);

  onMounted(async () => {
    await fetchEntries();
    scrollToBottom();
    setTimeout(() => {
      isLoading.value = false;
    }, 2700);

    setInterval(async () => {
      await fetchEntries();
      scrollToBottom();
    }, 4000);
  });

  const randomStarStyle = (n) => {
    const size = Math.random() * 2 + 1;
    const top = Math.random() * 100;
    const left = Math.random() * 100;
    const delay = Math.random() * 5;
    const duration = 2 + Math.random() * 3;
    return `top:${top}%; left:${left}%; width:${size}px; height:${size}px; animation-delay:${delay}s; animation-duration:${duration}s;`;
  };

  // bei X drücken wird zeit gespeichert
  const saveVisitTime = () => {
    localStorage.setItem("lastChatVisit", new Date().toISOString());

    const newMessages = ref(0);

    onMounted(async () => {
      let lastVisit = localStorage.getItem("lastChatVisit");
      if (!lastVisit) {
        lastVisit = new Date(0).toISOString();
        localStorage.setItem("lastChatVisit", lastVisit);
      }

      await journalStore.fetchEntries(authStore.authHeader);
      newMessages.value = journalStore.newMessageCount(lastVisit);
    });
  };
</script>

<style scoped>
  @import url("https://fonts.googleapis.com/css2?family=Nunito:wght@400;600&display=swap");

  body {
    font-family: "Nunito", sans-serif;
    margin: 0;
    background: #f0edf8;
  }

  .logo {
    position: absolute;
    top: 20px;
    left: 20px;
    width: 50px;
    z-index: 1000;
  }

  .chat-container {
    width: 80vw;
    max-width: 900px;
    height: 76vh;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: linear-gradient(to bottom, #f7f5ff, #ece9f7);
    border-radius: 24px;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
  }

  .close-button {
    position: absolute;
    top: 12px;
    right: 12px;
    background: transparent;
    border: none;
    font-size: 24px;
    color: #888494;
    cursor: pointer;
    padding: 0;
    line-height: 1;
    transition: color 0.2s ease;
  }

  .close-button:hover {
    color: #555;
  }

  .entries {
    flex: 1;
    padding: 16px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 14px;
    scroll-behavior: smooth;
  }

  .entry-card {
    max-width: 70%;
    padding: 14px 18px;
    background: white;
    border-radius: 18px 18px 18px 4px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    align-self: flex-start;
  }
  .entry-card.other-entry {
    align-self: flex-start;
    background: #ffffff;
    color: #333;
    border-radius: 18px 18px 18px 4px;
  }

  .entry-content p {
    margin: 0;
    word-wrap: break-word;
  }

  .timestamp {
    margin-top: 8px;
    font-size: 3px;
    color: rgba(0, 0, 0, 0.5);
    align-self: flex-end;
  }

  .entry-card.own-entry .timestamp {
    color: rgba(0, 0, 0, 0.7);
    align-self: flex-end;
  }

  .entry-card.other-entry .timestamp {
    align-self: flex-start;
    color: rgba(0, 0, 0, 0.4);
  }

  .entry-card.own-entry {
    align-self: flex-end;
    background: linear-gradient(to right, #d5c6f6, #c4a5f5);
    color: rgb(0, 0, 0);
    border-radius: 18px 18px 4px 18px;
  }

  .entry-card img {
    margin-top: 10px;
    max-width: 100%;
    border-radius: 12px;
    max-height: 170px;
    object-fit: cover;
  }

  .entry-card small {
    display: block;
    margin-top: 10px;
    color: #888;
    font-size: 12px;
  }

  .chat-form {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 14px 16px;
    background: #e5e0f0;
  }

  .chat-form textarea {
    flex: 1;
    border-radius: 16px;
    padding: 12px;
    resize: none;
    font-size: 15px;
    border: 1px solid #ccc;
    background: #fff;
  }

  .upload-btn {
    cursor: pointer;
    font-size: 20px;
  }

  .spinner {
    width: 26px;
    height: 26px;
    border: 4px solid #c0b2df;
    border-top: 4px solid #7e6aa8;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

  button[type="submit"] {
    background: linear-gradient(to right, #9b8cc3, #7e6aa8);
    color: white;
    border: none;
    border-radius: 50%;
    width: 44px;
    height: 44px;
    font-size: 18px;
    cursor: pointer;
  }

  .bottom-buttons {
    display: flex;
    justify-content: center;
    padding: 20px;
  }

  .nav-button {
    background-color: #9b8cc3;
    color: white;
    padding: 10px 25px;
    border-radius: 12px;
    font-size: 16px;
    text-decoration: none;
  }

  .animated-background {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(to bottom, #232946, #eebbc3);
    overflow: hidden;
    z-index: -1;
  }

  .cloud-svg {
    position: absolute;
    fill: url(#cloudGradient);
    opacity: 0.9;
    filter: drop-shadow(0 0 15px rgba(255, 255, 255, 0.3));
    animation: floatClouds linear infinite;
  }

  .cloud-1 {
    width: 200px;
    top: 10%;
    left: -220px;
    animation-duration: 70s;
  }
  .cloud-2 {
    width: 260px;
    top: 25%;
    left: -280px;
    animation-duration: 90s;
    animation-delay: 5s;
  }
  .cloud-3 {
    width: 220px;
    top: 40%;
    left: -250px;
    animation-duration: 110s;
    animation-delay: 10s;
  }
  .cloud-4 {
    width: 200px;
    top: 55%;
    left: -200px;
    animation-duration: 85s;
    animation-delay: 3s;
  }
  .cloud-5 {
    width: 250px;
    top: 70%;
    left: -240px;
    animation-duration: 100s;
    animation-delay: 7s;
  }
  .cloud-6 {
    width: 210px;
    top: 80%;
    left: -230px;
    animation-duration: 95s;
    animation-delay: 9s;
  }

  @keyframes floatClouds {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(160vw);
    }
  }

  .star {
    position: absolute;
    background: white;
    border-radius: 50%;
    opacity: 0.8;
    animation: twinkle infinite ease-in-out;
    box-shadow: 0 0 6px rgba(255, 255, 255, 0.8);
    z-index: -2;
  }

  @keyframes twinkle {
    0%,
    100% {
      opacity: 0.2;
      transform: scale(1);
    }
    50% {
      opacity: 1;
      transform: scale(1.6);
    }
  }

  .moon {
    position: absolute;
    top: 40px;
    right: 60px;
    width: 60px;
    height: 60px;
    background: radial-gradient(circle at 30% 30%, #fff, #ccc);
    border-radius: 50%;
    box-shadow: 0 0 20px #fff;
    z-index: -1;
  }

  .loading-logo {
    width: 80px;
    animation: pulse 1.5s ease-in-out infinite;
  }

  .loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(30, 30, 40, 0.6); /* kein Blur, nur Abdunklung */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 3000;
    animation: fadeIn 0.3s ease-in-out;
  }

  @keyframes pulse {
    0%,
    100% {
      transform: scale(1);
      opacity: 0.8;
    }
    50% {
      transform: scale(1.1);
      opacity: 1;
    }
  }
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  .blurred {
    filter: blur(5px);
    pointer-events: none;
  }
</style>
