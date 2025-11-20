import { createRouter, createWebHistory } from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import Register from "../views/Register.vue";
import ConnectionView from "@/views/ConnectionView.vue";
import { useAuthStore } from "@/stores/auth";
import DiaryView from "../views/DiaryView.vue";
import ChatView from "@/views/ChatView.vue";

const routes = [
  { path: "/login", name: "login", component: Login },
  { path: "/register", name: "register", component: Register },
  { path: "/", redirect: { name: "home" } },
  {
    path: "/home",
    name: "home",
    component: Home,
    meta: { requiresAuth: true },
  },
  {
    path: "/connection",
    name: "connection",
    component: ConnectionView,
    meta: { requiresAuth: true },
  },
  {
    path: "/diary",
    name: "diary",
    component: DiaryView,
    meta: { requiresAuth: true },
  },
  {
    path: "/chat",
    name: "chat",
    component: ChatView,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router;
