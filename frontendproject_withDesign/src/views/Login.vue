<template>
  <div class="login-wrapper">
    <div class="form-section">
      <img src="@/assets/2OfUs.ico" alt="heart icon" class="icon" />
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <input v-model="username" placeholder="Username" required />
        <input
          v-model="password"
          type="password"
          placeholder="Password"
          required
        />
        <button type="submit" :disabled="!canLogin">Login</button>
      </form>
      <p v-if="message" class="error">{{ message }}</p>
      <p>
        No account yet?
        <RouterLink to="/register" class="link">Sign up here</RouterLink>
      </p>
    </div>
    <div class="image-section">
      <img src="@/assets/get-started.png" alt="Login Illustration" />
    </div>
  </div>
</template>

<script setup>
  import { computed, ref } from "vue";
  import { useRouter, RouterLink } from "vue-router";
  import { useAuthStore } from "../stores/auth";

  const username = ref("");
  const password = ref("");
  const message = ref("");
  const router = useRouter();

  const authStore = useAuthStore();

  const canLogin = computed(
    () => username.value.trim() !== "" && password.value.trim() !== ""
  );

  async function handleLogin() {
    try {
      await authStore.login(username.value, password.value);
      router.push({ name: "home" });
    } catch (error) {
      message.value = error.message || "Failed to log in";
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

  h2 {
    font-family: "Pacifico", cursive;
    font-size: 1.8rem;
    margin-bottom: 20px;
    text-align: center;
    color: #392a6d;
  }

  .link {
    color: rgb(52, 166, 52);
    font-weight: bold;
    text-decoration: underline;
  }

  p {
    margin: 10px 0;
  }

  .login-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
    width: 100vw;
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e3cbf9);
  }

  .form-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px;
  }

  form {
    width: 100%;
    max-width: 300px;
    display: flex;
    flex-direction: column;
  }

  input {
    margin: 10px 0;
    padding: 10px;
    border-radius: 12px;
    border: 2px solid #ccc;
    font-size: 16px;
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
    margin-top: 20px;
    background-color: #9b8cc3;
    color: white;
    font-size: 16px;
    padding: 10px;
    border: none;
    border-radius: 15px;
    cursor: pointer;
  }

  button:enabled:hover {
    background-color: #7e6aa8;
  }

  .icon {
    width: 50px;
    margin-bottom: 20px;
  }

  .error {
    color: red;
    text-align: center;
    margin-top: 10px;
  }

  .image-section {
    flex: 1;
    height: 100%;
    display: flex;
  }

  .image-section img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  @media (max-width: 768px) {
    .image-section {
      display: none;
    }
  }
</style>
