<template>
  <div class="pending-request-box" v-if="sentRequest.status === 'PENDING'">
    <p>
      <strong>{{ sentRequest.receiverUsername }}</strong>
      has not yet responded to your invitation.
    </p>
  </div>
  <div class="declined-request-box" v-if="sentRequest.status === 'DECLINED'">
    <p>
      <strong>{{ sentDeclinedRequest.receiverUsername }}</strong>
      has declined your invitation.
    </p>
  </div>
</template>

<script setup>
  const props = defineProps({
    sentRequest: Object,
  });
</script>

<style scoped>
  .pending-request-box {
    background-color: rgb(255 255 255 / 70%);
    padding: 40px;
    border-radius: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    max-width: 800px;
    min-width: 618px;
    margin: 10px 0px;
    white-space: normal;
    overflow-wrap: break-word;
  }

  .declined-request-box {
    background-color: rgb(255 255 255 / 80%);
    padding: 40px;
    border-radius: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    max-width: 800px;
    min-width: 618px;

    margin: 10px 0;
    white-space: normal;
  overflow-wrap: break-word;
  }

  p {
    margin: 0 5px;
  }

  strong {
    font-weight: bold;
  }
</style>
