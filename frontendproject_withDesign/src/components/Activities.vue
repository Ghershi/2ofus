<template>
  <!-- Purple Box Wrapper -->
  <div class="activities-box">
    <div class="activities-container">
      <button
        class="activity-button"
        v-for="activity in activities"
        :key="activity.id"
        @click="selectActivity(activity)"
      >
        <img :src="activity.image" :alt="activity.title" />
        <h3 class="activity-title">{{ activity.title }}</h3>
      </button>
    </div>
  </div>

  <!-- Popup zum Einladen -->
  <div v-if="showPopUp" class="popup-overlay">
    <div class="popup">
      <h3>Invite to: {{ selectedActivity.title }}</h3>
      <textarea v-model="comment" placeholder="Write a Message..."></textarea>
      <div class="button-row">
        <button class="cancel-button" @click="deselectActivity">Cancel</button>
        <button @click="emitActivityInvite" class="invite-button">
          Send an invitation
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref } from "vue";

  const comment = ref("");
  const showPopUp = ref(false);
  const selectedActivity = ref(null);

  const toast = ref({
    show: false,
    message: "",
    type: "info",
  });

  const props = defineProps({
    activities: {
      type: Array,
      required: true,
    },
  });

  const emit = defineEmits(["inviteToActivity"]);

  function selectActivity(activity) {
    selectedActivity.value = activity;
    showPopUp.value = true;
  }

  function deselectActivity() {
    selectedActivity.value = null;
    showPopUp.value = false;
    comment.value = "";
  }

  function emitActivityInvite() {
    emit("inviteToActivity", {
      activity: selectedActivity.value.id,
      comment: comment.value,
    });
    showPopUp.value = false;
    comment.value = "";
  }
</script>

<style scoped>
  .logo {
    position: absolute;
    top: 20px;
    left: 20px;
    width: 50px;
    z-index: 1000;
  }

  /* NEW: Purple Box Styling */
  .activities-box {
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e0caf4);
    padding: 40px;
    max-width: 920px;
    width: 100%;
    max-height: 90vh; /* ensures it fits within viewport */
    overflow-y: auto; /* enables internal scrolling if content is too tall */
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
  }

  .activities-container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 40px 60px;
    width: 100%;
    box-sizing: border-box;
    justify-items: center;
  }

  .activity-button {
    background: #d5ddbe;
    border: none;
    padding: 0;
    text-align: center;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 30px;
    width: 100%;
    padding: 10px;
  }

  .activity-button img {
    width: 180px;
    height: 180px;
    object-fit: cover;
    border-radius: 30px;
    margin-bottom: 5px;
  }

  .activity-button:hover {
    transform: scale(1.05);
    transition: transform 0.2s ease;
  }

  .activity-title {
    font-family: "Pacifico", cursive;
    font-size: 1.3rem;
    font-weight: lighter;
    color: #392a6d;
  }

  .button-row {
    display: flex;
    justify-content: space-between;
    margin: 10px;
    flex-wrap: wrap;
  }

  .invite-button {
    background-color: #9b8cc3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 150px;
    margin: 0px 5px;
  }

  .invite-button:hover {
    background-color: #7e6aa8;
  }

  .cancel-button {
    background-color: #888494;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 150px;
    margin: 0px 5px;
  }

  .cancel-button:hover {
    background-color: #6c6976;
  }

  /* Keep existing popup/toast styles as-is */
  .popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(255, 255, 255, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }

  .popup {
    background: #fff;
    border-radius: 16px;
    padding: 20px;
    width: 90%;
    max-width: 400px;
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .popup input,
  .popup textarea {
    width: 100%;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ccc;
    font-size: 14px;
  }

  /* Responsive */
  @media (max-width: 1024px) {
    .activities-container {
      grid-template-columns: repeat(2, 1fr);
      gap: 30px 40px;
    }
  }

  @media (max-width: 600px) {
    .activities-container {
      grid-template-columns: repeat(1, 1fr);
      gap: 24px;
    }

    .activity-button img {
      max-width: 160px;
    }

    .logo {
      width: 40px;
      top: 12px;
      left: 12px;
    }
  }
</style>
