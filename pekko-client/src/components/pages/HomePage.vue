<template>
  <h1>Welcome {{ chatStore.loggedIn.username }}</h1>

</template>
<script setup lang="ts">
import { useChatStore } from '@/stores/chat';
const chatStore = useChatStore()

</script>
<style scoped></style>