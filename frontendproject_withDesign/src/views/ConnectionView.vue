[media pointer="file-service://file-XoD2nYvWHS9BBSm4cd8v4f"]
<template>
  <NavbarLogout />

  <div class="page-container">
    <div class="typing-wrapper">
      <h1 class="typing-text">
        <span class="invisible-text">Grow Your Connection</span>
        <span class="visible-text">{{ typedText }}</span>
      </h1>
    </div>

    <img src="@/assets/2OfUs.ico" alt="logo" class="logo-top" />

    <div class="header-row">
      <div class="main-content">
        <div class="left-panel">
          <div class="emotions">
            <img
              src="@/assets/thinking-of-you.png"
              alt="Thinking of You"
              class="emotion"
              :class="{ float: isThinking }"
              @click="
                toggleEmotion('thinking');
                triggerEmotion('thinking');
              "
            />
            <img
              src="@/assets/miss-you2.png"
              alt="Miss You"
              class="emotion"
              :class="{ pulse: isMissActive }"
              @click="
                toggleEmotion('miss');
                triggerEmotion('miss');
              "
            />
          </div>

          <div class="countdown-box">
            <div
              class="cloud-countdown"
              :style="{ backgroundImage: `url(${cloud})` }"
            >
              <p class="label">kiss me in...</p>
              <p class="value">{{ countdown }}</p>
            </div>
          </div>
          <div
            v-show="pendingInvites.length !== 0"
            class="love-envelope-wrapper"
          >
            <div
              class="envelope"
              @click="showActivityInvites"
              tabindex="0"
              aria-label="Show activity invites"
            >
              <div class="front flap"></div>
              <div class="front pocket"></div>
              <div class="letter">
                <div class="words">{{ envelopeText }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="right-panel">
          <button class="next-button" @click="toggleCalendar">
            Next Date ▼
          </button>
          <transition name="slide-fade">
            <div class="calendar-placeholder">
              <template v-if="!showCalendar">
                <figure class="polaroid" :class="{ isPlaceholder }">
                  <img :src="couplePicture" alt="Couple Picture" />
                  <div class="polaroid-overlay" v-if="isPlaceholder">
                    <button class="upload-couple-pic" @click="openUploadModal">
                      📷 Post your favorite couple picture.
                    </button>
                  </div>
                  <figcaption>
                    {{ formattedTargetDate }}
                  </figcaption>
                </figure>
              </template>
              <div v-else class="calendar-overlay">
                <CalendarComponent @select-date="setTargetDate" />
              </div>
            </div>
          </transition>
        </div>

        <div class="bottom-panel" v-if="activeActivities.length !== 0">
          <ActiveActivities
            :activities="activities"
            :activeActivities="activeActivities"
            :submittedActivityId="submittedActivityId"
            @select-activity="handleSelectActivity"
          />
        </div>
      </div>
    </div>
    <div class="button-row">
      <button @click="openActivitiesModal" class="diary-button">
        Activities
      </button>
      <router-link to="/diary" class="diary-button">Our Diary</router-link>
      <router-link to="/chat" class="diary-button"
        >Chat
        <span v-if="newMessages">({{ newMessages }} neu)</span></router-link
      >
    </div>

    <!-- 🔽 Modal for Invite -->
    <ModalComponent
      v-if="showActivityInvitesModal"
      @close="closeActivityInvitesModal"
      id="act-inv-modal"
    >
      <transition-group name="list-move" tag="div">
        <ActivityInvite
          v-for="activityInvite in pendingInvites"
          :key="activityInvite.activityInviteId"
          :activityInvite="activityInvite"
          :activities="activities"
          @acceptActivityInvite="
            handleAcceptingInvite(activityInvite.activityInviteId)
          "
          @declineActivityInvite="
            handleDecliningInvite(activityInvite.activityInviteId)
          "
        />
      </transition-group>
    </ModalComponent>

    <!-- Modal: Other Activities -->
    <ModalComponent v-if="showModal" @close="closeModal">
      <Activities
        :activities="activities"
        @inviteToActivity="handleActivityInvite"
      />
    </ModalComponent>

    <!--Modal: Upload Ongoing Activities-->
    <ModalComponent
      v-if="showUploadForm"
      @close="closeUploadForm"
      id="submission-modal"
    >
      <div class="upload-form">
        <form @submit.prevent="handleActivitySubmission">
          <label class="file-upload">
            <input type="file" accept="image/*" @change="onFileChange" hidden />
            <span>📷 Select Image</span>
          </label>
          <div v-if="imageUrl">
            <Cropper
              class="cropper"
              :src="imageUrl"
              :stencil-props="{
                aspectRatio: 1,
                movable: true,
                scalable: true,
                resizable: true,
              }"
              :resize-image="true"
              @change="onCrop"
            />

            <button
              type="button"
              @click="submitCrop"
              :disabled="!croppedCanvas"
              style="margin: 10px"
            >
              Select section
            </button>
          </div>
          <div>
            <img
              v-if="croppedBlob"
              :src="croppedUrl"
              alt="Preview"
              style="margin-top: 10px; max-width: 200px"
            />
          </div>
          <textarea
            rows="2"
            cols="29"
            type="text"
            placeholder="Your Caption..."
            v-model="caption"
            id="submission-caption"
          ></textarea>

          <button type="submit" :disabled="!croppedBlob || !caption">
            Upload
          </button>
        </form>
      </div>
    </ModalComponent>

    <ModalComponent v-if="showUploadCouplePic" @close="closeUploadModal">
      <div class="upload-form">
        <form @submit.prevent="handleCouplePicturePosting">
          <label class="file-upload">
            <input type="file" accept="image/*" @change="onFileChange" hidden />
            <span>📷 Select Image</span>
          </label>
          <div v-if="imageUrl">
            <Cropper
              class="cropper"
              :src="imageUrl"
              :stencil-props="{
                aspectRatio: 1,
                movable: true,
                scalable: true,
                resizable: true,
              }"
              :resize-image="true"
              @change="onCrop"
            />

            <button
              type="button"
              @click="submitCrop"
              :disabled="!croppedCanvas"
              style="margin: 10px"
            >
              Select section
            </button>
          </div>
          <div>
            <img
              v-if="croppedBlob"
              :src="croppedUrl"
              alt="Preview"
              style="margin-top: 10px; max-width: 200px"
            />
          </div>
          <button type="submit" v-if="croppedBlob" class="upload-btn-cp">
            Upload
          </button>
        </form>
      </div>
    </ModalComponent>

    <div
      class="overlay"
      v-if="
        showModal ||
        showActivityInvitesModal ||
        showUploadForm ||
        showUploadCouplePic
      "
    ></div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted, onBeforeUnmount, watch } from "vue";
  import CalendarComponent from "@/components/Calendar.vue";
  import ModalComponent from "@/components/ModalComponent.vue";
  import Activities from "@/components/Activities.vue";
  import ActivityInvite from "@/components/ActivityInvite.vue";
  import ActiveActivities from "@/components/ActiveActivities.vue";
  import { useAuthStore } from "@/stores/auth";
  import { useConnectionStore } from "@/stores/connection";
  import placeholderImage from "@/assets/coupletothestars.png";
  import cloud from "@/assets/cloudcountdown.png";
  import confetti from "canvas-confetti";
  import NavbarLogout from "@/components/NavbarLogout.vue";
  import { activities } from "@/components/ActivitiesArray";
  import { Cropper } from "vue-advanced-cropper";
  import "vue-advanced-cropper/dist/style.css";
  import { useJournalStore } from "@/stores/journal";

  // Typing animation
  const fullText = "Grow Your Connection";
  const typedText = ref("");
  let typingIndex = 0;
  let typingTimer = null;

  const startTyping = () => {
    typedText.value = "";
    typingIndex = 0;
    typingTimer = setInterval(() => {
      if (typingIndex < fullText.length) {
        typedText.value += fullText[typingIndex];
        typingIndex++;
      } else {
        clearInterval(typingTimer);
      }
    }, 120);
  };

  onMounted(() => {
    startTyping();
  });

  // UI state
  const showModal = ref(false);
  const showActivityInvitesModal = ref(false);
  const showCalendar = ref(false);
  const showUploadForm = ref(false);
  const showUploadCouplePic = ref(false);

  // Stores
  const authStore = useAuthStore();
  const connectionStore = useConnectionStore();

  // Data
  const activityInvites = ref([]);
  const pendingInvites = ref([]);
  const activeActivities = ref([]);

  activities;

  // Compute Envelope Text

  const envelopeText = computed(() => {
    if (pendingInvites.value.length === 1) {
      return "1 invite";
    } else {
      return `${pendingInvites.value.length} invites`;
    }
  });

  // Modal logic
  function openActivitiesModal() {
    showModal.value = true;
  }
  function closeModal() {
    showModal.value = false;
  }
  function showActivityInvites() {
    showActivityInvitesModal.value = true;
  }
  function closeActivityInvitesModal() {
    showActivityInvitesModal.value = false;
  }
  function closeModalWhenNoUnansweredInvites(id) {
    pendingInvites.value = pendingInvites.value.filter(
      (invite) => invite.activityInviteId !== id
    );
    if (pendingInvites.value.length === 0) {
      setTimeout(() => {
        showActivityInvitesModal.value = false;
      }, 500);
    }
  }

  function closeUploadForm() {
    showUploadForm.value = false;
    caption.value = "";
    ongoingActivityId.value = null;
    imageUrl.value = null;
    croppedCanvas.value = null;
    croppedUrl.value = null;
    croppedBlob.value = null;
  }

  //Activity Invite

  async function handleActivityInvite(payload) {
    const url = "http://localhost:8080/api/activity/invite";

    try {
      const res = await fetch(url, {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          activityId: payload.activity,
          connection: connectionStore.connection.id,
          comment: payload.comment,
        }),
      });

      const data = await res.json();

      if (!res.ok) {
        console.log(data.message);
      } else {
        console.log(data.message);
      }
    } catch (e) {
      console.error(e);
    }
  }

  // Fetch invites

  const invitePollingTimer = ref(null);

  onMounted(() => {
    fetchActivityInvites(
      connectionStore.connection.id,
      connectionStore.connectedUser.userId
    );

    invitePollingTimer.value = window.setInterval(() => {
      fetchActivityInvites(
        connectionStore.connection.id,
        connectionStore.connectedUser.userId
      );
    }, 5000);
  });

  watch(activityInvites, (newVal) => {
    pendingInvites.value = [...newVal];
  });

  onBeforeUnmount(() => {
    if (invitePollingTimer.value !== null) {
      clearInterval(invitePollingTimer.value);
    }
  });

  async function fetchActivityInvites(id, userId) {
    const url = `http://localhost:8080/api/activity/pending&active?connectionid=${id}&userid=${userId}`;
    try {
      const res = await fetch(url, { headers: authStore.authHeader });
      if (!res.ok) throw new Error(`Response status: ${res.status}`);
      const data = await res.json();
      activityInvites.value = data.pendingActivityInvites;
      activeActivities.value = data.activeActivityInvites;
      console.log(activityInvites.value.length);
    } catch (error) {
      console.error("Unable to load activity invites:", error);
      activityInvites.value = [];
    }
  }

  // Invite response
  async function handleAcceptingInvite(id) {
    try {
      await respondToActivityInvite(id, "ACTIVE");
      await fetchActivityInvites(
        connectionStore.connection.id,
        connectionStore.connectedUser.userId
      );
      closeModalWhenNoUnansweredInvites(id);
    } catch (error) {
      console.error(error);
    }
  }
  async function handleDecliningInvite(id) {
    try {
      await respondToActivityInvite(id, "DECLINED");
      closeModalWhenNoUnansweredInvites(id);
    } catch (error) {
      console.error(error);
    }
  }
  async function respondToActivityInvite(id, status) {
    const url = `http://localhost:8080/api/activity/response?activityInviteId=${id}`;
    try {
      const res = await fetch(url, {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ activityStatus: status }),
      });
      if (!res.ok) throw new Error();
    } catch (error) {
      console.error(error);
    }
  }

  // Activity submission
  const ongoingActivityId = ref(null);
  const submittedActivityId = ref(null);

  function handleSelectActivity(id) {
    showUploadForm.value = true;
    ongoingActivityId.value = id;
  }

  const imageUrl = ref(null);
  const croppedCanvas = ref(null);
  const croppedUrl = ref(null);
  const croppedBlob = ref(null);
  const caption = ref("");
  const photoUrl = ref("");

  function onFileChange(event) {
    const file = event.target.files[0];
    if (file) {
      imageUrl.value = URL.createObjectURL(file);
      croppedUrl.value = null;
      croppedCanvas.value = null;
      croppedBlob.value = null;
    }
  }

  function onCrop({ canvas }) {
    croppedCanvas.value = canvas;
    if (canvas) {
      croppedUrl.value = canvas.toDataURL("image/png");
    }
  }

  function submitCrop() {
    if (croppedCanvas.value) {
      imageUrl.value = null;
      croppedCanvas.value.toBlob((blob) => {
        croppedBlob.value = blob;
      }, "image/png");
    }
  }

  async function handleActivitySubmission() {
    await uploadImage();
    await submitActivity(ongoingActivityId.value, caption.value);
    closeUploadForm();
  }

  async function uploadImage() {
    if (!croppedBlob.value) {
      console.error("Kein zugeschnittenes Bild zum Hochladen");
      return;
    }

    const formData = new FormData();
    formData.append("image", croppedBlob.value, "cropped.png");

    try {
      const res = await fetch("http://localhost:8080/api/images/upload", {
        method: "POST",
        body: formData,
      });
      const data = await res.json();
      if (!res.ok) {
        console.error("Failed to upload image " + res.status);
      } else {
        photoUrl.value = data.url;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async function submitActivity(id, caption) {
    try {
      const res = await fetch("http://localhost:8080/api/activity/submission", {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          activityInvite: id,
          caption: caption,
          photoUrl: photoUrl.value,
        }),
      });
      if (!res.ok) {
        photoUrl.value = "";
        console.error("Failed to submit activity " + res.status);
      } else {
        photoUrl.value = "";
        submittedActivityId.value = id;
      }
    } catch (error) {
      photoUrl.value = "";
      console.error(error);
    }
  }

  // Calendar & countdown
  const targetDate = ref(null);
  const countdown = ref("Select a date");

  const formattedTargetDate = computed(() => {
    if (!targetDate.value) return "";
    const d = targetDate.value;
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  });

  const toggleCalendar = () => {
    showCalendar.value = !showCalendar.value;
  };

  const setTargetDate = (date) => {
    targetDate.value = new Date(date);
    showCalendar.value = false;
    submitCountdownDate();
    updateCountdown();
  };

  function parseLocalDate(dateString) {
    const [year, month, day] = dateString.split("-").map(Number);
    return new Date(year, month - 1, day); // Erzeugt Datum um 00:00 Lokalzeit
  }

  function updateCountdown() {
    const now = new Date();

    if (!targetDate.value || targetDate.value <= now) {
      countdown.value = "Select a date";
      return;
    }

    const diff = targetDate.value - now;

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((diff / (1000 * 60)) % 60);
    const seconds = Math.floor((diff / 1000) % 60);

    countdown.value = `${days} days ${hours} hr ${minutes} min ${seconds} sec`;
  }

  async function submitCountdownDate() {
    try {
      const d = targetDate.value;
      if (!d) return;

      const dateOnly = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(
        2,
        "0"
      )}-${String(d.getDate()).padStart(2, "0")}`;

      const res = await fetch("http://localhost:8080/api/countdown/save", {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ countdownDate: dateOnly }),
      });

      if (!res.ok) throw new Error("Failed to save countdown date");
      console.log("Countdown date saved:", dateOnly);
    } catch (error) {
      console.error("Error saving countdown date:", error);
    }
  }

  async function fetchCountdownDate() {
    try {
      const res = await fetch("http://localhost:8080/api/countdown/get", {
        headers: authStore.authHeader,
      });

      if (!res.ok) throw new Error("Failed to fetch countdown date");

      const data = await res.json();
      if (data.countdownDate) {
        const parsedDate = parseLocalDate(data.countdownDate);

        // Prüfe, ob das Datum gültig und in der Zukunft ist
        if (
          parsedDate instanceof Date &&
          !isNaN(parsedDate.getTime()) &&
          parsedDate > new Date()
        ) {
          targetDate.value = parsedDate;
        } else {
          targetDate.value = null; // Kein gültiges zukünftiges Datum → kein Countdown
        }
      } else {
        targetDate.value = null; // Kein Datum vorhanden → kein Countdown
      }
    } catch (error) {
      console.error("Error fetching countdown date:", error);
      targetDate.value = null;
    }

    updateCountdown();
  }

  let timer;
  onMounted(async () => {
    await fetchCountdownDate(); // Setzt targetDate (oder null)
    timer = setInterval(updateCountdown, 1000);
  });

  onBeforeUnmount(() => {
    clearInterval(timer);
  });
  // Emotion triggers
  const isThinking = ref(false);
  const isMissActive = ref(false);
  const justTriggered = ref(false);
  const lastSeenTriggerTime = ref(null);
  let emotionPollingTimer;

  onMounted(async () => {
    await fetchInitialEmotionStatus();
    emotionPollingTimer = setInterval(pollEmotionStatus, 500);
  });
  onBeforeUnmount(() => {
    clearInterval(emotionPollingTimer);
  });

  const toggleEmotion = (type) => {
    if (type === "thinking") {
      isThinking.value = true;
      isMissActive.value = false;
      setTimeout(() => (isThinking.value = false), 4000);
    } else {
      isMissActive.value = true;
      isThinking.value = false;
      setTimeout(() => (isMissActive.value = false), 4000);
    }
  };

  function triggerConfetti() {
    confetti({
      particleCount: 100,
      spread: 70,
      origin: { y: 0.6 },
    });
  }

  async function triggerEmotion(type) {
    try {
      justTriggered.value = true;

      const res = await fetch(
        "http://localhost:8080/api/countdown/emotions/trigger",
        {
          method: "POST",
          headers: {
            ...authStore.authHeader,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ type }),
        }
      );
      if (!res.ok) throw new Error();

      // Das Backend sollte dir das echte triggeredAt zurückgeben:
      const { triggeredAt } = await res.json();
      const emotionKey = `${type}-${triggeredAt}`;
      localStorage.setItem("lastSeenEmotionKey", emotionKey);
      lastSeenTriggerTime.value = emotionKey;

      if (type === "thinking") {
        isThinking.value = true;
      } else {
        isMissActive.value = true;
      }

      triggerConfetti();

      setTimeout(() => {
        isThinking.value = false;
        isMissActive.value = false;
        setTimeout(() => {
          justTriggered.value = false;
        }, 10000);
      }, 1000);
    } catch (error) {
      console.error("Error triggering emotion:", error);
      justTriggered.value = false;
    }
  }

  async function pollEmotionStatus() {
    if (justTriggered.value) return;

    try {
      const res = await fetch(
        "http://localhost:8080/api/countdown/emotions/status",
        {
          method: "GET",
          headers: authStore.authHeader,
        }
      );

      if (!res.ok) return;

      const data = await res.json();
      const { type, triggeredAt } = data;
      if (!type || !triggeredAt) return;

      // Neu: Nur reagieren, wenn neuer Triggerzeitpunkt erkannt wird
      if (triggeredAt !== lastSeenTriggerTime.value) {
        const triggeredTime = new Date(triggeredAt).getTime();
        const now = Date.now();

        // Nur wenn Emotion wirklich *jetzt* getriggert wurde (z.B. in den letzten 5 Sekunden)
        if (now - triggeredTime <= 1000) {
          lastSeenTriggerTime.value = triggeredAt;

          if (type === "thinking") {
            isThinking.value = true;
          } else if (type === "miss") {
            isMissActive.value = true;
          }

          triggerConfetti();

          setTimeout(() => {
            isThinking.value = false;
            isMissActive.value = false;
          }, 1000);
        }
      }
    } catch (error) {
      console.error("Polling error:", error);
    }
  }

  async function fetchInitialEmotionStatus() {
    try {
      const res = await fetch(
        "http://localhost:8080/api/countdown/emotions/status",
        {
          method: "GET",
          headers: authStore.authHeader,
        }
      );
      if (!res.ok) return;

      const data = await res.json();
      const { type, triggeredAt } = data;
      if (!type || !triggeredAt) return;

      const emotionKey = `${type}-${triggeredAt}`;
      const seenKey = localStorage.getItem("lastSeenEmotionKey");

      if (emotionKey !== seenKey) {
        localStorage.setItem("lastSeenEmotionKey", emotionKey);
        lastSeenTriggerTime.value = emotionKey;

        if (type === "thinking") {
          isThinking.value = true;
        } else {
          isMissActive.value = true;
        }

        triggerConfetti();
        setTimeout(() => {
          isThinking.value = false;
          isMissActive.value = false;
        }, 1000);
      }
    } catch (error) {
      console.error("Initial emotion status fetch error:", error);
    }
  }

  // neue Nachrichten/Messages anzeigen
  const journalStore = useJournalStore();
  const newMessages = ref(0);

  let intervalId = null;

  const checkNewMessages = async () => {
    const lastVisit =
      localStorage.getItem("lastChatVisit") || new Date(0).toISOString();
    await journalStore.fetchEntries(authStore.authHeader);
    newMessages.value = journalStore.newMessageCount(lastVisit);
  };

  onMounted(() => {
    checkNewMessages(); // direkt beim Mount ausführen
    intervalId = setInterval(checkNewMessages, 5000); // alle 5 Sekunden
  });

  onBeforeUnmount(() => {
    clearInterval(intervalId);
  });

  // Couple Picture

  onMounted(() => {
    fetchCouplePicture();
  });

  function openUploadModal() {
    showUploadCouplePic.value = true;
  }

  function closeUploadModal() {
    showUploadCouplePic.value = false;
    imageUrl.value = null;
    croppedCanvas.value = null;
    croppedUrl.value = null;
    croppedBlob.value = null;
  }

  const couplePicture = ref(null);

  const isPlaceholder = computed(
    () => couplePicture.value === placeholderImage
  );

  async function fetchCouplePicture() {
    try {
      const res = await fetch("http://localhost:8080/api/couplepic/get", {
        method: "GET",
        headers: authStore.authHeader,
      });

      if (res.status === 404) {
        couplePicture.value = placeholderImage;
      } else if (res.ok) {
        const data = await res.json();
        couplePicture.value = data.photoUrl;
      } else {
        couplePicture.value = placeholderImage;
        console.error(res.status);
      }
    } catch (error) {
      couplePicture.value = placeholderImage;
      console.error(error.message);
    }
  }

  async function handleCouplePicturePosting() {
    await uploadImage();
    await postCouplePicture();
    await fetchCouplePicture();
    closeUploadModal();
  }

  async function postCouplePicture() {
    try {
      const res = await fetch("http://localhost:8080/api/couplepic/save", {
        method: "POST",
        headers: {
          ...authStore.authHeader,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          photoUrl: photoUrl.value,
        }),
      });
      if (!res.ok) {
        console.error("Failed to upload picture." + res.status);
        photoUrl.value = "";
      } else {
        photoUrl.value = "";
      }
    } catch (error) {
      photoUrl.value = "";
      console.error(error);
    }
  }
</script>

<style scoped>
  .page-container {
    min-height: 100vh;
    width: 100vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e3cbf9);

    overflow-y: auto;
    position: relative;
  }

  .logo-top {
    position: absolute;
    top: 20px;
    left: 20px;
    width: 50px;
    z-index: 1000;
  }

  .typing-wrapper {
    margin-top: 50px;
    margin-bottom: 15px;
    text-align: center;
    padding: 0px 16px;
    width: 100%;
    box-sizing: border-box;
  }

  .typing-text {
    position: relative;
    padding: 1.3px 0;
    font-family: "Pacifico", cursive;
    font-size: 2.4rem;
    color: #392a6d;
    white-space: nowrap;
    overflow: hidden;
    width: fit-content;
    margin: auto;
    animation: blink-caret 0.7s step-end infinite;
  }

  .typing-wrapper::after {
    content: "";
    display: block;
    width: 760px;
    height: 4px;
    background: linear-gradient(to right, #cc7fe1, #7f9be1);
    margin: 12px auto 0;
    border-radius: 2px;
  }

  .invisible-text {
    visibility: hidden;
    display: inline-block;
  }

  .visible-text {
    position: absolute;
    top: 0;
    left: 0;
  }

  @keyframes blink-caret {
    0%,
    100% {
      border-color: transparent;
    }
    50% {
      border-color: #392a6d;
    }
  }

  .header-row {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .main-content {
    display: flex;
    justify-content: center;
    align-items: stretch;
    gap: 24px;
    flex-wrap: wrap;
    width: 100%;
    max-width: 1350px; /* oder eine andere sinnvolle Zahl */
    margin: 0 auto;
  }

  .left-panel,
  .right-panel,
  .bottom-panel {
    flex: 1 1 0;
    min-width: 280px;
    min-height: 400px;
    max-height: 480px;
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    padding: 24px;

    display: flex;
    flex-direction: column;
    margin: 0;
  }

  .bottom-panel {
    overflow-y: auto;
  }

  @media (min-width: 1200px) {
    .main-content {
      flex-wrap: nowrap;
    }
    .left-panel,
    .right-panel,
    .bottom-panel {
      max-width: 450px;
    }
  }

  @media (max-width: 1199px) {
    .main-content {
      flex-wrap: wrap;
    }
    .left-panel,
    .right-panel {
      max-width: 450px;
      flex: 1 1 450px;
    }
    .bottom-panel {
      flex-basis: 100%;
      max-width: 924px;
      min-width: 0;
      min-height: 120px;
    }
  }

  @media (max-width: 950px) {
    .left-panel,
    .right-panel,
    .bottom-panel {
      max-width: 450px;
      flex-basis: 100%;
    }
  }

  .emotions {
    display: flex;
    gap: 80px;
    justify-content: center;
    width: 100%;
  }

  .emotion {
    width: 100px;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .emotion.float {
    animation: float 2s infinite ease-in-out;
  }

  .emotion.pulse {
    animation: pulse 1.2s infinite;
  }

  .countdown-box {
    width: 100%;
    max-width: 500px;
    font-family: "Segoe UI", sans-serif;
    text-align: center;
  }

  .cloud-countdown {
    flex-direction: column;
    width: auto;
    margin-top: -6px;
    margin-bottom: 9px;
    height: 260px;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .countdown-box .label {
    margin-top: 2rem;
    font-family: "Pacifico", cursive;
    font-size: 25px;
    font-weight: 800;
    color: #392a6d;
  }

  .countdown-box .value {
    font-size: 27px;
    font-weight: 800;
    color: white;
    text-shadow: -1px -1px 0 black, 1px -1px 0 black, -1px 1px 0 black,
      1px 1px 0 black;
  }

  .button-row {
    display: flex;
    gap: 20px;
    width: 919px; /* feste Breite, passend zu den Panels */
    margin: 8px auto 0; /* zentriert */
    box-sizing: border-box; /* wichtig */
  }

  .button-row button,
  .button-row a.diary-button {
    flex: 1; /* alle gleich breit */
    margin-top: 13px;
    padding: 10px 0;
    font-size: 16px;
    border-radius: 12px;
    border: none;
    cursor: pointer;
    background-color: #9b8cc3;
    color: white;
    text-align: center;
    text-decoration: none;
    box-sizing: border-box;
  }

  .button-row button:hover,
  .button-row a.diary-button:hover {
    background-color: #7e6aa8;
  }

  /* Button-Design bleibt wie bei dir */
  .next-button,
  .diary-button {
    padding: 10px 0; /* gleiche Höhe wie alle anderen */
    margin-bottom: 5px;
    background-color: #9b8cc3;
    color: white;
    font-size: 16px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    text-align: center;
    text-decoration: none;
  }

  .next-button:hover,
  .diary-button:hover {
    background-color: #7e6aa8;
  }

  @keyframes shake {
    0%,
    100% {
      transform: rotate(0deg);
    }
    20%,
    60% {
      transform: rotate(-3deg);
    }
    40%,
    80% {
      transform: rotate(3deg);
    }
  }

  .emotion:hover {
    animation: shake 0.5s ease-in-out infinite;
  }

  .calendar-placeholder {
    position: relative;
    width: 100%;
    max-width: 300px;
    min-height: 370px;
    display: flex;

    justify-content: center;
    align-items: center;
    margin: auto;
  }

  .calendar-image {
    width: 100%;
    border-radius: 12px;
    object-fit: cover;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  }

  .countdown-overlay {
    position: absolute;
    bottom: 35px;
    left: 50%;
    transform: translateX(-50%);
    padding: 8px 16px;
    border-radius: 12px;
    font-family: "Pacifico", cursive;
    font-size: 20px;
    color: #392a6d;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
  }

  .calendar-overlay {
    position: relative;
    background: white;
    border-radius: 12px;
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    min-width: 300px;

    justify-content: center;
    align-items: center;
    margin: auto;
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

  #act-inv-modal,
  #submission-modal {
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e0caf4);
    box-sizing: border-box;
    margin: 0;
    padding: 25px;
  }

  .list-move-move {
    transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .list-move-leave-active {
    transition: opacity 0.5s, transform 0.5s;
    position: absolute;
    width: 100%;
  }

  .list-move-leave-to {
    opacity: 0;
    transform: translateX(100px);
  }

  .love-envelope-wrapper {
    margin-top: 10px;
    display: flex;
    justify-content: center;
  }

  .envelope {
    position: relative;
    width: 140px;
    height: 90px;
    cursor: pointer;
    perspective: 1000px;
  }
  .envelope .flap,
  .envelope .pocket {
    position: absolute;
    width: 0;
    height: 0;
  }

  .envelope .flap {
    position: absolute;
    top: 0;
    left: 0;
    border-left: 70px solid transparent;
    border-right: 70px solid transparent;
    border-bottom: 41px solid transparent;
    border-top: 49px solid #d9534f;
    transform-origin: top;
    transition: transform 0.4s ease;
    z-index: 2;
  }

  /* Pocket (bottom trapezoid) */
  .envelope .pocket {
    position: absolute;
    bottom: 0;
    left: 0;
    border-left: 70px solid #f5a3a2;
    border-right: 70px solid #f5a3a2;
    border-bottom: 45px solid #ff6f61;
    border-top: 45px solid transparent;
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
    z-index: 1;
  }

  /* Letter inside the envelope */
  .envelope .letter {
    position: absolute;
    top: 12px;
    left: 15px;
    width: 110px;
    height: 65px;
    background: white;
    border-radius: 4px;
    padding: 6px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
    text-align: center;
    z-index: 0;
    opacity: 0;
    transform: translateY(20px);
    transition: opacity 0.4s ease, transform 0.4s ease;
    pointer-events: none;
  }

  .envelope .letter .words {
    margin: 4px 0;
    font-size: 0.8rem;
    font-family: "Pacifico", cursive;
  }

  /* Envelope opened state */
  .envelope:hover .flap {
    transform: rotateX(180deg);
    z-index: 1;
  }

  .envelope:hover .letter {
    opacity: 1;
    transform: translateY(-15px);
    z-index: 3;
  }

  /* Responsive styles */
  @media (max-width: 768px) {
    .header-row,
    .main-content {
      flex-direction: column;
      align-items: center;
    }

    .footer {
      flex-direction: column;
      gap: 10px;
    }

    .calendar-overlay {
      top: 48px;
      left: 0;
      right: auto;
    }

    .emotions {
      flex-wrap: wrap;
      gap: 40px;
    }

    .emotion {
      width: 80px;
    }

    .envelope {
      width: 120px;
      height: 80px;
    }
    .envelope .letter {
      opacity: 0;
      transform: translateY(20px);
      transition: opacity 0.4s ease, transform 0.4s ease;
    }

    .envelope.open .letter {
      opacity: 1;
      transform: translateY(-10px);
    }
    @keyframes slide-up {
      0% {
        opacity: 0;
        transform: translateY(20px);
      }
      100% {
        opacity: 1;
        transform: translateY(-10px);
      }
    }

    .envelope.open .letter {
      animation: slide-up 0.5s ease forwards;
    }
  }

  /* Uplaod-Form for activity submissions*/
  .upload-form {
    background: #fff;
    border-radius: 16px;
    padding: 25px;
    margin: 8px;
    width: 90%;
    min-width: 450px;
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .upload-form form {
    display: flex;
    flex-direction: column;
  }

  .upload-form input,
  .upload-form textarea {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 10px;
    border: 1px solid #ccc;
    font-size: 14px;
  }

  .upload-form form button {
    background-color: #9b8cc3;
    color: #fff;
    padding: 10px;
    border: none;
    border-radius: 12px;
    font-size: 16px;
    cursor: pointer;
    text-align: center;
    text-decoration: none;
    width: 120px;
    align-self: flex-end;
  }

  .upload-form form button:disabled {
    background-color: #ddd8ea;
    color: white;
    font-size: 16px;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: not-allowed;
    width: 120px;
    align-self: flex-end;
  }

  .upload-form form button:enabled:hover {
    background-color: #7e6aa8;
  }

  .file-upload {
    display: inline-block;
    padding: 10px 15px;
    margin: 10px 0;
    background-color: #9b8cc3;
    color: white;
    border-radius: 5px;
    cursor: pointer;
  }

  .file-upload:hover {
    background-color: #7e6aa8;
  }

  submission-caption {
    margin: 10px 0;
    padding: 10px;
    border-radius: 12px;
    border: 2px solid #ccc;
    font-size: 16px;
    background: white;
    transition: border-color 0.3s;
  }

  #submission-caption:focus {
    border: 3px solid transparent;
    background-image: linear-gradient(white, white),
      linear-gradient(to right, #cc7fe1, #7f9be1);
    background-origin: border-box;
    background-clip: padding-box, border-box;
    outline: none;
  }

  .love-envelope-wrapper {
    position: relative; /* Bezugspunkt für .envelope */
  }

  .envelope {
    position: absolute; /* nicht fixed */
    top: 5px;
    right: 152px;
    width: 70px;
    height: 45.5px;
    cursor: pointer;
    perspective: 1000px;
    z-index: 1100;
  }

  /* Flap */
  .envelope .flap {
    border-left: 35px solid transparent; /* 70% von 50px */
    border-right: 35px solid transparent;
    border-bottom: 21px solid transparent; /* 70% von 30px */
    border-top: 24.5px solid #d9534f; /* 70% von 35px */
    transform-origin: top;
    transition: transform 0.4s ease;
    z-index: 2;
  }

  /* Pocket */
  .envelope .pocket {
    border-left: 35px solid #f5a3a2;
    border-right: 35px solid #f5a3a2;
    border-bottom: 23.1px solid #ff6f61; /* 70% von 33px */
    border-top: 23.1px solid transparent; /* 70% von 33px */
    border-bottom-left-radius: 2.8px; /* 70% von 4px */
    border-bottom-right-radius: 2.8px;
    z-index: 1;
  }

  /* Letter */
  .envelope .letter {
    position: absolute;
    top: 6px; /* 70% von 9px */
    left: 8px; /* 70% von 11px */
    width: 56px; /* 70% von 80px */
    height: 35px; /* 70% von 50px */
    font-size: 0.77rem; /* 70% von 1.1rem */
    padding: 3.5px; /* 70% von 5px */
    background: white;
    border-radius: 4px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
    text-align: center;
    z-index: 0;
    opacity: 0;
    transform: translateY(20px);
    transition: opacity 0.4s ease, transform 0.4s ease;
    pointer-events: none;
  }

  /* Polaroid-Rahmen */
  .polaroid {
    background: #fff;
    padding: 16px 16px 52px 16px; /* unten mehr Abstand (typisch Polaroid) */
    margin: 32px;
    display: inline-block;
    box-shadow: 2px 6px 18px 0 rgba(60, 60, 60, 0.25);
    border-radius: 6px;
    position: relative;
    max-width: 300px;
  }

  /* Bild selbst */
  .polaroid img {
    width: 100%;
    display: block;
    border-radius: 3px;
    box-shadow: 0 2px 8px rgba(80, 80, 80, 0.08);
  }

  /* Caption/Bildunterschrift */
  .polaroid figcaption {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 16px;
    padding: 0 16px;
    text-align: center;
    font-family: "Pacifico", cursive;
    font-size: 1.3em;
    color: #392a6d;
    letter-spacing: 0.01em;
    pointer-events: none;
    opacity: 0.86;
  }

  .polaroid-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.8);
    opacity: 0;
    transition: opacity 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    pointer-events: none;
  }

  .isPlaceholder:hover .polaroid-overlay {
    opacity: 1;
    pointer-events: all;
  }

  .polaroid-overlay-text {
    font-size: 1.2em;
    text-align: center;
    padding: 18px 10px;
    user-select: none;
  }

  .upload-couple-pic {
    display: inline-block;
    padding: 10px 15px;
    margin: 20px;
    background-color: white;
    border-radius: 12px;
    cursor: pointer;
    text-align: center;
    border: 5px solid transparent;
    background-image: linear-gradient(white, white),
      linear-gradient(to right, #cc7fe198, #7f9be19f);
    background-origin: border-box;
    background-clip: padding-box, border-box;
    outline: none;
  }

  .upload-form-cp {
    min-width: 350px;
    min-height: 100px;
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }

  .cropper {
    max-width: 800px;
    max-height: 400px;
  }

  .upload-btn {
    padding: 10px; /* gleiche Höhe wie alle anderen */
    margin-bottom: 5px;
    background-color: #9b8cc3;
    color: white;
    font-size: 16px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    text-align: center;
    text-decoration: none;
  }
</style>
