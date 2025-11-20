<template>



  <div v-if="!userIsConnected">

    <NavbarLogout/>
    <div class="home-wrapper">
      <!-- Romantischer Hintergrund mit Herzregen -->
      <div class="background-animation">
        <div class="hearts" :class="{ 'background-paused': isTyping }">
          <div
            class="heart"
            v-for="n in 50"
            :key="n"
            :style="randomHeartStyle(n)"
          ></div>
        </div>
      </div>

      <div class="content-box">

        <h3 class="typewriter-text">{{ typewriterText }}</h3>

        <hr class="animated-divider" />

        <p>
          A true connection doesn't need physical closeness,<br />
          two hearts willing to stay together.<br />
          Find people who believe in the kind of love you do,<br>
          and connect now with your loved ones!<br><br>
          Something special starts here ...
        </p>
        <button class="search-button" @click="openSearchModal">
          <img src="../assets/twohearts2.png" alt="two hearts" />
          <span class="button-text">Start your journey</span>
        </button>
      </div>

      <div v-if="requests">
        <ReceivedRequests
          v-for="receivedRequest in receivedRequests"
          :key="receivedRequest.requestId"
          :received-request="receivedRequest"
          :handleAccept="() => acceptRequest(receivedRequest.requestId)"
          :handleDecline="() => declineRequest(receivedRequest.requestId)"
        />
        <SentRequests
          v-for="sentRequest in sentRequests"
          :key="sentRequest.requestId"
          :sent-request="sentRequest"
        />
      </div>
    </div>

    <ModalComponent v-if="showModal" @close="closeModal">
      <Search @inviteUser="handleInviteUser" />
    </ModalComponent>
    <div class="overlay" v-if="showModal"></div>
  </div>
</template>

<script setup>
  import { ref, onMounted, watch, computed} from "vue";
  import { useRouter } from "vue-router";
  import { onUnmounted } from "vue";
  import { useAuthStore } from "@/stores/auth";
  import { useConnectionStore } from "@/stores/connection";
  import { useToastStore } from "@/stores/toast";
  import ReceivedRequests from "@/components/ReceivedRequests.vue";
  import SentRequests from "@/components/SentRequests.vue";
  import ModalComponent from "@/components/ModalComponent.vue";
  import Search from "@/components/Search.vue";
  import NavbarLogout from "@/components/NavbarLogout.vue";


  const router = useRouter();
  const authStore = useAuthStore();
  const connectionStore = useConnectionStore();
  const toast = useToastStore();

  const username = ref("");
  const typewriterText = ref("");
  const fullText = ref("");
  const isTyping = ref(false);

  const userIsConnected = ref();
  const showModal = ref(false);
  const requests = ref([]);
  const receivedRequests = ref([]);
  const sentRequests = ref([]);



  function randomHeartStyle(n) {
    const left = Math.random() * 100;
    const size = Math.random() * 10 + 10;
    const delay = Math.random() * 10;
    const duration = Math.random() * 5 + 5;
    return `
    left: ${left}%;
    width: ${size}px;
    height: ${size}px;
    animation-delay: ${delay}s;
    animation-duration: ${duration}s;
  `;
  }

  const pollingInterval = ref(null);
  let connectionCheckInterval = null;

  onMounted(async () => {

    username.value = authStore.currentUser?.username;
    const firstText = `Welcome ${username.value}`;
    fullText.value = firstText;
    startTyping();
    toastmessage()

    await checkIfUserConnected(); // ← Direkt prüfen

    if (userIsConnected.value) {
      await connectionStore.fetchConnectionData();
      return router.push({ name: "connection" }); // ← Sofort weiterleiten
    }

    connectionCheckInterval = setInterval(() => {
      if (!userIsConnected.value) {
        checkIfUserConnected();
      }
    }, 3000);

    pollingInterval.value = setInterval(() => {
      if (!userIsConnected.value) {
        fetchAllRequests();
      }
    }, 3000);
  });

  onUnmounted(() => { if (pollingInterval.value !== null) {
    clearInterval(pollingInterval.value);
  }
    if (connectionCheckInterval !== null) {
      clearInterval(connectionCheckInterval);
    }
  });

  function toastmessage() {

    toast.show('Connect & say Hi !');
  }

  function startTyping() {
    typewriterText.value = "";
    isTyping.value = true;

    let i = 0;
    const text = fullText.value;

    function typeChar() {
      if (i < text.length) {
        typewriterText.value += text[i];
        i++;

        let delay = 40 + Math.random() * 60; // 40–100ms
        if ([".", "!", "?"].includes(text[i - 1]))
          delay = 400; // pause after punctuation
        else if (text[i - 1] === ",") delay = 200;

        setTimeout(() => requestAnimationFrame(typeChar), delay);
      } else {
        isTyping.value = false;
      }
    }

    requestAnimationFrame(typeChar);
  }

  watch(userIsConnected, async () => {
    if (userIsConnected.value) {
      await connectionStore.fetchConnectionData();
      router.push({ name: "connection" });
    } else {
      fetchAllRequests();
    }
  });

  function openSearchModal() {
    showModal.value = true;
  }
  function closeModal() {
    showModal.value = false;
  }

  async function handleInviteUser(user) {
    const url = `http://localhost:8080/api/invitation/send?receiverId=${user.userId}`;
    try {
      const res = await fetch(url, {
        method: "POST",
        headers: authStore.authHeader,
      });
      const data = await res.json();
      if (res.ok) {
        closeModal();
        fetchAllRequests();
      } else toast.show(data.error);
    } catch (error) {
      console.error(error);
    }
  }

  async function checkIfUserConnected() {
    try {
      const res = await fetch("http://localhost:8080/api/connection/check", {
        method: "GET",
        headers: authStore.authHeader,
      });
      if (!res.ok) throw new Error(`Status: ${res.status}`);
      userIsConnected.value = await res.json();
    } catch (error) {
      console.error(error);
    }
  }

  async function fetchAllRequests() {
    try {
      const res = await fetch("http://localhost:8080/api/invitation/listAll", {
        method: "GET",
        headers: authStore.authHeader,
      });
      if (!res.ok) throw new Error(`Status: ${res.status}`);
      requests.value = await res.json();
    } catch (error) {
      console.error(error);
    }
  }

  watch(requests, () => {
    receivedRequests.value = requests.value.filter(
      (r) => r.receiverId === authStore.currentUser.userId
    );
    sentRequests.value = requests.value.filter(
      (r) => r.senderId === authStore.currentUser.userId
    );
  });

  async function acceptRequest(id) {
    await respondToRequest(id, "ACCEPTED");
    userIsConnected.value = true;
  }
  async function declineRequest(id) {
    await respondToRequest(id, "DECLINED");
    await fetchAllRequests();
  }

  async function respondToRequest(id, status) {
    try {
      await fetch(`http://localhost:8080/api/invitation?requestId=${id}`, {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ requestStatus: status }),
      });
    } catch (error) {
      console.error(error);
    }
  }
</script>

<style scoped>
  html,
  body {
    margin: 0;
    padding: 0;
    height: 100%;
    width: 100%;
  }
  * {
    box-sizing: border-box;
  }

  .home-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100vh;
    width: 100vw;
    position: relative;
    overflow: hidden;
  }

  .background-animation {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e0caf4);
    background-size: 300% 300%;
    animation: gradientFlow 30s ease infinite;
    z-index: -1;
  }

  .background-paused .heart {
    animation-play-state: paused !important;
  }

  .hearts {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
  }

  .heart {
    position: absolute;
    background-color: rgba(252, 172, 196, 0.85);
    transform: rotate(45deg);
    animation-name: dropHeart;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    opacity: 0.7;
  }
  .heart::before,
  .heart::after {
    content: "";
    position: absolute;
    background-color: rgba(252, 172, 196, 0.85);
    border-radius: 50%;
  }
  .heart::before {
    top: -50%;
    left: 0;
    width: 100%;
    height: 100%;
  }
  .heart::after {
    left: -50%;
    top: 0;
    width: 100%;
    height: 100%;
  }

  @keyframes gradientFlow {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }
  @keyframes twinkle {
    0%,
    100% {
      opacity: 0.8;
    }
    50% {
      opacity: 0.2;
    }
  }
  @keyframes dropHeart {
    0% {
      transform: translateY(-20vh) scale(0.5) rotate(45deg);
      opacity: 0;
    }
    50% {
      opacity: 0.9;
    }
    100% {
      transform: translateY(110vh) scale(1.2) rotate(45deg);
      opacity: 0;
    }
  }

  .content-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    padding: 40px;
    position: relative;
    z-index: 1;
  }

  .logo {
    width: 60px;
    margin-bottom: 20px;
  }

  p {
    font-size: 1.4rem;
    font-family: "Pacifico", cursive;
    margin-bottom: 15px;
    color: #392a6d;
  }

  .typewriter-text {
    font-size: 36px;
    font-family: "Pacifico", cursive;
    color: #392a6d;

    text-shadow: 1px 1px 3px #e0c3fc;
    white-space: pre-line;
    overflow: visible;
    margin-bottom: 20px;
    line-height: 1.6;
    padding-bottom: 10px;
  }

  @keyframes blink {
    0%,
    100% {
      opacity: 1;
    }
    50% {
      opacity: 0;
    }
  }

  .search-button {
    background: transparent;
    width: 200px;
    height: 140px;
    border: none;
    position: relative;
    cursor: pointer;
  }

  .search-button img {
    height: 100%;
    width: 100%;
    object-fit: cover;
    display: block;
  }

  .button-text {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-family: "Pacifico", cursive;
    font-size: 1.7em;
    text-shadow: 2px 2px 5px rgb(0, 0, 0, 0.7);
    pointer-events: none; /* Damit der Text keine Klicks abfängt */
  }

  .search-button:hover {
    animation: pulse 0.6s infinite;
  }

  @keyframes pulse {
    0% {
      transform: scale(1);
    }
    70% {
      transform: scale(1.08);
    }
    100% {
      transform: scale(1);
    }
  }

  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.75);
    z-index: 1;
    pointer-events: auto;
  }

  .content-box {
    width: 100%;
    max-width: 617px;
    margin: 40px auto 20px;
    padding: 24px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    text-align: center;
    animation: fadeIn 1.5s ease;


  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(15px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }}

  .typewriter-text {
    font-size: 36px;
    font-family: "Pacifico", cursive; /* ursprüngliche romantische Schrift */
    color: #392a6d;
    text-shadow: 1px 1px 3px #e0c3fc;
    white-space: pre-line;
    overflow: visible;
    margin-bottom: 20px;
    line-height: 1.6;
    padding-bottom: 10px;
  }


  p {
    font-family: 'Inter', sans-serif;
    font-size: 1.1rem;
    line-height: 1.6;
    color: #4a3d7a;
    margin-bottom: 24px;
    text-shadow: 0 1px 1px rgba(255, 255, 255, 0.4);
  }

  .search-button {
    background: transparent;
    width: 220px;
    height: 150px;
    border: none;
    position: relative;
    cursor: pointer;
    transition: transform 0.3s ease;
  }

  .search-button:hover {
    transform: scale(1.05);
  }

  .button-text {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-family: "Pacifico", cursive;
    font-size: 1.8em;
    text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.7);
    pointer-events: none;
  }
  .animated-divider {
    border: none;
    height: 3px;
    width: 60%;

    margin: 32px auto;
    margin-top: -15px;
    background: linear-gradient(90deg, #fcacc4, #e0caf4, #b0c2f3, #fcacc4);
    background-size: 300% 100%;
    animation: shimmer 3.5s ease-in-out infinite;
    border-radius: 4px;
    opacity: 0.85;
    box-shadow: 0 0 6px rgba(252, 172, 196, 0.5);
  }

  @keyframes shimmer {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }
  .content-box {
    width: 100%;
    max-width: 617px;
    margin: 40px auto 20px;
    padding: 24px;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    text-align: center;
    animation: fadeIn 1.5s ease, floatBox 6s ease-in-out infinite;
    border: 1px solid rgba(255, 255, 255, 0.3);
    box-shadow:
        0 0 10px rgba(252, 172, 196, 0.3),
        0 0 20px rgba(224, 202, 244, 0.3),
        0 0 30px rgba(176, 194, 243, 0.2);
    transition: box-shadow 0.3s ease;
  }

  .content-box:hover {
    box-shadow:
        0 0 12px rgba(252, 172, 196, 0.6),
        0 0 25px rgba(224, 202, 244, 0.5),
        0 0 35px rgba(176, 194, 243, 0.4);
  }

  @keyframes floatBox {
    0%, 100% {
      transform: translateY(0px);
    }
    50% {
      transform: translateY(-8px);
    }
  }

  .typewriter-text {
    font-size: 36px;
    font-family: "Pacifico", cursive;
    color: #392a6d;
    text-shadow:
        0 0 8px rgba(224, 202, 244, 0.6),
        1px 1px 3px #e0c3fc,
        0 0 2px #392a6d;
    white-space: pre-line;
    overflow: visible;
    margin-bottom: 20px;
    line-height: 1.6;
    padding-bottom: 10px;
  }
</style>
