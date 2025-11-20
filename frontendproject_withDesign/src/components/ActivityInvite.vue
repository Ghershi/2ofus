<template>
  <transition name="slide-fade">
    <div v-if="show" class="activity-invite-box">
      <img :src="invitedActivity.image" alt="invitedActivity.title" />
      <div>
        <p id="activity-title">{{ invitedActivity.title }}</p>
        <p>
          {{ activityInvite.comment }}
        </p>
      </div>
      <div class="react-btn">
        <button class="acceptButton" @click="emitAccept">Yes!</button>
        <button class="declineButton" @click="emitDecline">No.</button>
      </div>
    </div>
  </transition>
</template>

<script setup>
  import { ref, computed } from "vue";

  const show = ref(true);

  const props = defineProps({
    activityInvite: {
      type: Object,
      required: true,
    },
    activities: {
      type: Array,
      required: true,
    },
  });

  const invitedActivity = computed(() =>
    props.activities.find((a) => a.id === props.activityInvite.activityId)
  );

  const emit = defineEmits(["acceptActivityInvite", "declineActivityInvite"]);

  function emitAccept() {
    show.value = false;
    emit("acceptActivityInvite");
  }

  function emitDecline() {
    show.value = false;
    emit("declineActivityInvite");
  }
</script>

<style scoped>
  .activity-invite-box {
    background-color: rgb(255 255 255);
    padding: 10px 20px;
    border-radius: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    max-width: 800px;
    min-width: 450px;
    margin: 10px 0px;
  }

  img {
    width: 100px;
    border-radius: 30px;
    border: 5px solid #d5ddbe;
    margin: 10px;
  }

  .acceptButton {
    background-color: #9b8cc3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 100px;
    margin: 5px 5px;
  }

  .acceptButton:hover {
    background-color: #7e6aa8;
  }

  .declineButton {
    background-color: #888494;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 100px;
    margin: 5px 5px;
  }

  .declineButton:hover {
    background-color: #6c6976;
  }

  #activity-title {
    font-family: "Pacifico", cursive;
    font-size: 1.1rem;
    font-weight: lighter;
  }

  p {
    margin: 10px 15px;
  }

  .react-btn {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
  }

  .slide-fade-leave-active {
    transition: opacity 0.5s ease, transform 0.5s ease;
  }

  .slide-fade-leave-to {
    opacity: 0;
    transform: translateX(100px);
  }

  .slide-fade-leave-from {
    opacity: 1;
    transform: translateX(0);
  }
</style>
