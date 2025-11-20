<template>
  <router-view />
  <ToastContainer />
</template>

<script setup>
  import ToastContainer from "./components/ToastContainer.vue";
</script>

<style>
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

  body {
    font-family: "Segoe UI", sans-serif;
    background-color: #fdf5e6;
  }
</style>
