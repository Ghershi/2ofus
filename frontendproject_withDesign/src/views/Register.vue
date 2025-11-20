<template>
  <div class="register-wrapper">
    <div class="form-section">
      <img src="@/assets/2OfUs.ico" alt="heart icon" class="icon" />
      <h2>Register</h2>
      <form @submit.prevent="register">
        <input v-model="username" placeholder="Username" required />
        <input
          v-model="password"
          type="password"
          placeholder="Password"
          required
        />
        <input v-model="email" type="email" placeholder="Email" required />
        <input
          v-model.number="age"
          type="number"
          min="13"
          max="99"
          placeholder="Age"
          required
        />
        <button type="submit" :disabled="!canRegister">Register</button>
      </form>
      <p v-if="message" class="error">{{ message }}</p>
      <p>Login <RouterLink to="/login" class="link">here</RouterLink>.</p>
    </div>
    <div class="image-section">
      <img src="@/assets/register.png" alt="Register Illustration" />
    </div>
  </div>
</template>

<script setup>
  import { ref, computed } from "vue";
  import { useRouter, RouterLink } from "vue-router";

  const router = useRouter();

  const username = ref("");
  const password = ref("");
  const email = ref("");
  const age = ref(null);
  const message = ref("");

  const canRegister = computed(
    () =>
      username.value.trim() !== "" &&
      password.value.trim() !== "" &&
      email.value.trim() !== "" &&
      age.value !== ""
  );

  async function register() {
    const res = await fetch("http://localhost:8080/api/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
        email: email.value,
        age: age.value,
      }),
    });

    const data = await res.json();
    if (res.ok) {
      router.push({ name: "login" });
      message.value = data.message;
    } else {
      message.value = data.error || "An error occurred!";
    }

    username.value = "";
    password.value = "";
    email.value = "";
    age.value = "";
  }
</script>

<style scoped>
  .register-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
    width: 100vw;
    background: linear-gradient(135deg, #f8dbf1, #b0c2f3, #e3cbf9);
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
    max-width: 320px;
    display: flex;
    flex-direction: column;
  }

  input {
    padding: 10px;
    margin: 10px 0;
    border-radius: 12px;
    border: 1px solid #ccc;
    font-size: 16px;
    background: #fff;
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
    margin-top: 10px;
    text-align: center;
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
