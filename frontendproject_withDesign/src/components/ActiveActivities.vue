<template>
  <div v-if="ongoingActivities.length" class="active-activities-wrapper">
    <div class="activities-text">
      <h3>Your ongoing activities</h3>
      <p>
        Snap a photo during the activity and upload it to fill your diary with
        special moments.
      </p>
    </div>
    <div>
      <transition-group name="list-move" tag="div" class="activities-list">
        <div
          v-for="activity in ongoingActivities"
          :key="activity.activityInviteId"
          class="ongoing-activity"
        >
          <h4>{{ activity.title }}</h4>
          <div class="deadline">
            <button
              class="upload-btn"
              @click="emitSelectActivity(activity.activityInviteId)"
            >
              <img
                src="../assets/pic-upload-icon.png"
                alt="image uploading icon"
              />
            </button>
            <p>
              Upload a picture until:<br />
              <time :datetime="activity.deadline">{{
                formatDeadline(activity.deadline)
              }}</time>
            </p>
          </div>
        </div>
      </transition-group>
    </div>
  </div>
</template>

<script setup>
  import { ref, watch } from "vue";

  const props = defineProps({
    activeActivities: {
      type: Array,
      required: true,
    },
    activities: {
      type: Array,
      required: true,
    },
    submittedActivityId: {
      type: Object,
      required: false,
    },
  });

  const ongoingActivities = ref([]);

  watch(
    () => props.activeActivities,
    (newVal) => {
      const activities = [...newVal];
      ongoingActivities.value = activities
        .map((obj) => ({
          ...obj,
          title: computeTitle(obj),
        }))
        .sort((a, b) => new Date(a.deadline) - new Date(b.deadline));
    },
    { immediate: true }
  );

  watch(
    () => props.submittedActivityId,
    (newVal) => {
      ongoingActivities.value = ongoingActivities.value.filter(
        (a) => a.activityInviteId !== newVal
      );
    }
  );

  function computeTitle(obj) {
    const activity = props.activities.find((a) => a.id === obj.activityId);
    return activity.title;
  }

  function formatDeadline(deadline) {
    const date = new Date(deadline);
    return date.toLocaleString("de-DE", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  const emit = defineEmits(["selectActivity"]);

  function emitSelectActivity(id) {
    emit("selectActivity", id);
  }
</script>

<style scoped>
  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
  }

  .list-move-enter-active,
  .list-move-leave-active {
    transition: opacity 0.5s, transform 0.5s;
  }
  .list-move-enter-from,
  .list-move-leave-to {
    opacity: 0;
    transform: translateX(100px);
  }
  .list-move-enter-to,
  .list-move-leave-from {
    opacity: 1;
    transform: translateX(0);
  }

  .ongoing-activity {
    padding: 10px;
    margin: 10px;
    background-color: white;
    border-radius: 30px;
    display: flex;
    width: 290px;
    flex-direction: column;
    align-items: center;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  }

  .upload-btn {
    padding: 0;
    background-color: transparent;
    color: #fff;
    border: none;
    border-radius: 5px;
    width: 55px;
    height: 55px;
    align-content: center;
    margin: 0;
    cursor: pointer;
  }

  .upload-btn img {
    width: 48px;
    align-self: center;
  }

  .deadline {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 16px;
    width: 100%;
    justify-content: center;
  }

  .deadline p {
    margin: 0;
    text-align: left;
  }

  h3 {
    font-family: "Pacifico", cursive;
    color: #392a6d;
    font-size: 1.6rem;
    margin-bottom: 11px;
  }

  h4 {
    font-family: "Pacifico", cursive;
    color: #392a6d;
    font-size: 1.3rem;
    text-align: center;
    width: 100%;
    margin-bottom: 10px;
  }
  p {
    margin-bottom: 5px;
  }

  .activities-list {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    justify-content: center;
    align-items: center;
  }

  .active-activities-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  @media (max-width: 500px) {
    .upload-form {
      padding: 10px;
      max-width: 98vw;
    }
  }
</style>
