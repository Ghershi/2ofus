<template>
  <div class="request-box">
    <p>
      Yay! You have received an invitation from
      <strong>{{ receivedRequest.senderUsername }}</strong
      >!
    </p>
    <button class="acceptButton" @click="handleAccept(receivedRequest)">
      Accept
    </button>
    <button class="declineButton" @click="handleDecline(receivedRequest)">
      Decline
    </button>
  </div>
</template>

<script setup>
import { useToastStore } from "@/stores/toast";

const toastStore = useToastStore();

  const props = defineProps({
    receivedRequest: Object,
    handleAccept: Function,
    handleDecline: Function,
  });

  function handleAccept() {
    toastStore.show('You & Me - connected!');

    setTimeout(() => {
      props.handleAccept();
    }, 1000); // 1 Sekunde warten
  }

  function handleDecline() {
    props.handleDecline();
  }
</script>

<style scoped>
  .request-box {
    background-color: rgb(255 255 255 / 70%);
    padding: 30px;
    border-radius: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    max-width: 800px;
    min-width: 618px;
    margin: 10px 0px;
  }

  .acceptButton {
    background-color: #9b8cc3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 100px;
    margin: 0px 5px;
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
    margin: 0px 5px;
  }

  .declineButton:hover {
    background-color: #6c6976;
  }

  p {
    margin: 0px 5px;
  }

  strong {
    font-weight: bold;
  }
</style>
