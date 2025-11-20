<template>
  <div class="invite-wrapper">
    <div class="invite-box">
      <h2>Search</h2>
      <label for="searchUser"
        >Who do you want to send an invite to connect with you?</label
      >
      <input
        v-model="searchText"
        type="search"
        id="searchUser"
        placeholder="Enter the username."
      />
      <button :disabled="!userFound" @click="emitInviteUser">
        Send Invite
      </button>
    </div>
    <img
      src="@/assets/hug-illustration.png"
      alt="illustration of two persons hugging"
      class="side-img"
    />
  </div>
</template>

<script setup>
  import { ref, onMounted, watch } from "vue";
  import { useAuthStore } from "@/stores/auth";

  const searchText = ref("");
  const users = ref([]);
  const userFound = ref(false);
  const searchedUser = ref("");

  const authStore = useAuthStore();

  onMounted(async () => {
    const url = "http://localhost:8080/api/user/list";
    try {
      const res = await fetch(url, {
        method: "GET",
        headers: authStore.authHeader,
      });

      if (!res.ok) {
        throw new Error(`Response status: ${res.status}`);
      }

      users.value = await res.json();
    } catch (error) {
      console.log(error);
    }
  });

  watch(searchText, () => {
    const searchedUsername = searchText.value.trim().toLowerCase();
    const user = users.value.find(
      (u) => u.username.toLowerCase() === searchedUsername
    );

    if (user) {
      userFound.value = true;
      searchedUser.value = user;
    } else {
      userFound.value = false;
      searchedUser.value = null;
    }
  });

  const emit = defineEmits(["inviteUser"]);

  function emitInviteUser() {
    emit("inviteUser", { ...searchedUser.value });
  }
</script>

<style scoped>
  .invite-wrapper {
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e0caf4);
    box-sizing: border-box;
    margin: 0;
    padding: 20px;
    width: 100%;
    height: 100%;
    border-radius: 20px;
  }
  .invite-box {
    padding: 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
  }

  .logo {
    width: 50px;
    margin-bottom: 20px;
  }

  h2 {
    font-family: "Pacifico", cursive;
    color: #392a6d;
    font-size: 1.6rem;
    margin-bottom: 20px;
    text-align: center;
  }

  input {
    width: 100%;
    padding: 12px;
    border-radius: 15px;
    border: 1px solid #ccc;
    margin-bottom: 20px;
    font-size: 16px;
  }

  :disabled {
    background-color: #ddd8ea;
    color: white;
    font-size: 16px;
    padding: 10px 25px;
    border: none;
    border-radius: 12px;
    cursor: not-allowed;
    width: 100%;
  }

  button {
    background-color: #9b8cc3;
    color: white;
    font-size: 16px;
    padding: 10px 25px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    width: 100%;
  }

  button:enabled:hover {
    background-color: #7e6aa8;
  }

  .side-img {
    width: 200px;
    max-width: 40%;
  }

  label {
    margin-bottom: 15px;
  }

  input {
    border: 2px solid #ccc;
    background: white;
    transition: border-color 0.3s;
  }

  input:focus {
    border: 3px solid transparent;
    background-image: linear-gradient(white, white),
      linear-gradient(to right, #cc7fe1, #7f9be1);
    background-origin: border-box;
    background-clip: padding-box, border-box;
    outline: none;
  }

  @media (max-width: 768px) {
    .invite-wrapper {
      flex-direction: column;
      text-align: center;
    }

    .side-img {
      margin-top: 30px;
      margin-left: 0;
      width: 150px;
      max-width: 80%;
    }

    .invite-box {
      padding: 20px;
      max-width: 90%;
    }
  }
</style>
