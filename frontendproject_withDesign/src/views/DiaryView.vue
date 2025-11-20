<template>
  <NavbarLogout></NavbarLogout>
  <div class="page-wrapper">
    <div class="typing-wrapper">
      <router-link
        to="/connection"
        class="arrow-left"
        aria-label="Back"
      ></router-link>
      <h1 class="typewriter">{{ typewriterText }}</h1>
    </div>
    <Flipbook :submissions="submissions" />
  </div>
  <img src="@/assets/2OfUsKopie.png" alt="Logo" class="logo" />
</template>

<script setup>
  import { ref, onMounted, watch } from "vue";
  import Flipbook from "../components/Flipbook.vue";
  import { useAuthStore } from "../stores/auth";
  import { useConnectionStore } from "@/stores/connection";
  import { activities } from "../components/ActivitiesArray";
  import NavbarLogout from "@/components/NavbarLogout.vue";

  const authStore = useAuthStore();
  const connectionStore = useConnectionStore();
  const completedActivities = ref([]);
  const activitySubmissions = ref([]);
  const submissions = ref([]);

  activities;

  onMounted(() => {
    fetchCompletedActivities(connectionStore.connection.id);
    startTyping();
  });

  async function fetchCompletedActivities(id) {
    const url =
      "http://localhost:8080/api/activity/listcompleted?connectionid=" + id;
    try {
      const res = await fetch(url, {
        method: "GET",
        headers: authStore.authHeader,
      });

      const data = await res.json();

      completedActivities.value = data.completedActivities;
      activitySubmissions.value = data.submissions;

      if (!res.ok) {
        throw new Error(`Response status: ${res.status}`);
      }
    } catch (error) {
      console.error(error);
    }
  }

  function computeActivityTitle(submission) {
    const completedActivity = completedActivities.value.find(
      (c) => c.activityInviteId === submission.activityInvite
    );
    const activityType = activities.find(
      (a) => a.id === completedActivity.activityId
    );
    return activityType.title;
  }

  function formatSubmissionDate(submittedAt) {
    const date = new Date(submittedAt);
    return date.toLocaleString("de-DE", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  }

  function computeUsername(userId) {
    if (userId === authStore.currentUser.userId) {
      return "you";
    } else {
      return connectionStore.connectedUser.username;
    }
  }

  const typewriterText = ref("");
  let interval = null;

  function startTyping() {
    typewriterText.value = "";
    const fullText = "You & me, our Diary";
    if (interval) clearInterval(interval);
    let i = 0;
    interval = setInterval(() => {
      if (i <= fullText.length) {
        typewriterText.value = fullText.slice(0, i);
        i++;
      } else {
        clearInterval(interval);
      }
    }, 100);
  }

  watch(
    activitySubmissions,
    (newVal) => {
      const subs = [...newVal];
      submissions.value = subs
        .map((obj) => ({
          ...obj,
          user: computeUsername(obj.userDTO.userId),
          date: formatSubmissionDate(obj.submittedAt),
          title: computeActivityTitle(obj),
        }))
        .sort((a, b) => a.activityInvite - b.activityInvite);
    },
    { immediate: true }
  );
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
  .logo {
    position: absolute;
    top: 20px;
    left: 20px;
    width: 50px;
    z-index: 1000;
  }

  .page-wrapper {
    height: 100vh;
    width: 100vw;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e3cbf9);

    overflow: hidden;
    position: relative;
  }

  .typewriter {
    position: relative;
    padding: 1.3px 0;
    font-family: "Pacifico", cursive;
    font-size: 2.4rem;
    color: #392a6d;
    white-space: nowrap;
    overflow: hidden;
    width: fit-content;
    margin: auto;
    padding: 10px;
    animation: blink-caret 0.7s step-end infinite;
  }
  @keyframes typing {
    from {
      width: 0;
    }
    to {
      width: 100%;
    }
  }
  @keyframes blink-caret {
    0%,
    100% {
      border-color: transparent;
    }
    50% {
      border-color: black;
    }
  }

  .typing-wrapper {
    margin-top: 50px;
    margin-bottom: 15px;
    text-align: center;
    padding: 0px 16px;
    width: 100%;
    box-sizing: border-box;
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

  .arrow-left {
    position: absolute;
    top: 75px;
    left: 150px;
    width: 24px;
    height: 24px;
    border: solid #392a6d;
    border-width: 0 0 4px 4px;
    transform: rotate(45deg);
    cursor: pointer;
    transition: transform 0.2s ease, opacity 0.2s ease;
  }

  .arrow-left:hover {
    transform: rotate(45deg) translateX(-4px);
    opacity: 0.8;
  }
</style>
