import { defineStore } from "pinia";
import { ref, watch } from "vue";
import { useAuthStore } from "./auth";

export const useConnectionStore = defineStore("connection", () => {
  const connection = ref(null);
  const connectedUser = ref(null);
  const errorMessage = ref("");
  const authStore = useAuthStore();

  const stored = localStorage.getItem("connectionStore");

  if (stored) {
    try {
      const parsed = JSON.parse(stored);
      connection.value = parsed.connection || null;
      connectedUser.value = parsed.connectedUser || null;
    } catch (error) {
      console.error("Fehler beim Laden aus localStorage:", error);
    }
  }

  watch(
    [connection, connectedUser],
    () => {
      localStorage.setItem(
        "connectionStore",
        JSON.stringify({
          connection: connection.value,
          connectedUser: connectedUser.value,
        })
      );
    },
    { deep: true }
  );

  async function fetchConnectionData() {
    const url = "http://localhost:8080/api/connection/data";
    try {
      const res = await fetch(url, {
        method: "GET",
        headers: authStore.authHeader,
      });

      if (!res.ok) {
        throw new Error(`Response status: ${res.status}`);
      }

      const data = await res.json();
      connection.value = data.connectionData;
      connectedUser.value = data.connectedUser;
    } catch (error) {
      console.error(error);
    }
  }

  // Verbindung zurücksetzen
  function clearConnection() {
    connection.value = null;
    localStorage.removeItem("connectionStore");
  }

  return {
    connection,
    connectedUser,
    errorMessage,
    fetchConnectionData,
    clearConnection,
  };
});
