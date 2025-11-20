import { defineStore } from "pinia";
import { ref, watch, computed } from "vue";

export const useAuthStore = defineStore("auth", () => {
  const currentUser = ref(null);
  const jwtToken = ref(null);

  const stored = localStorage.getItem("authStore");

  if (stored) {
    try {
      const parsed = JSON.parse(stored);
      currentUser.value = parsed.currentUser || null;
      jwtToken.value = parsed.jwtToken || null;
    } catch (error) {
      console.error("Failed to load from localStorage:", error);
    }
  }

  watch(
    [currentUser, jwtToken],
    () => {
      localStorage.setItem(
        "authStore",
        JSON.stringify({
          currentUser: currentUser.value,
          jwtToken: jwtToken.value,
        })
      );
    },
    { deep: true }
  );

  const authHeader = computed(() =>
    jwtToken.value ? { Authorization: `Bearer ${jwtToken.value}` } : {}
  );

  const isTokenExpired = computed(() => {
    if (!jwtToken.value) return true;
    try {
      const payload = JSON.parse(atob(jwtToken.value.split(".")[1]));
      return Date.now() >= payload.exp * 1000;
    } catch {
      return true;
    }
  });

  const isAuthenticated = computed(
    () => !!jwtToken.value && !isTokenExpired.value
  );

  async function login(username, password) {
    try {
      const res = await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (!res.ok) {
        throw new Error(data.error || "Login failed");
      }

      jwtToken.value = data.token;
      currentUser.value = {
        userId: data.userId,
        username: data.username,
        userEmail: data.userEmail,
      };
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  }


  function logout() {
    jwtToken.value = null;

    currentUser.value = null;


    localStorage.removeItem("authStore");
  }

  return {
    jwtToken,
    currentUser,
    isAuthenticated,
    authHeader,
    isTokenExpired,
    login,
    logout,
  };
});
