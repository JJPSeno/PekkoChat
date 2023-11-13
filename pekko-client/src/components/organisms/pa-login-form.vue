<template>
  <form name="loginForm" method="POST" @submit.prevent="chatStore.login">
  <pa-labeled-input labeledInputId="login-input-username" v-model="chatStore.credentialsForm.username">
  Username:
  </pa-labeled-input>
  <pa-labeled-input labeledInputId="login-input-password" v-model="chatStore.credentialsForm.password">
  Password:
  </pa-labeled-input>
  <paButton btnClass="login" btnName="login" btnType="submit">Login</paButton>
  </form>
</template>
<script setup lang="ts">
import paLabeledInput from '../molecules/pa-labeled-input.vue';
import paButton from '../atoms/pa-button.vue';
import { useChatStore } from '@/stores/chat';

const chatStore = useChatStore()

</script>
<style scoped></style>