<template>
  <form name="registerationForm" method="POST" @submit.prevent="chatStore.register">
  <pa-labeled-input labeledInputId="registration-input-username" v-model="chatStore.credentialsForm.username">
  Username:
  </pa-labeled-input>
  <pa-labeled-input labeledInputId="registration-input-password" v-model="chatStore.credentialsForm.password">
  Password:
  </pa-labeled-input>
  <paButton btnClass="registration" btnName="registration" btnType="submit">Register</paButton>
  </form>
</template>
<script setup lang="ts">
import paLabeledInput from '../molecules/pa-labeled-input.vue';
import paButton from '../atoms/pa-button.vue';

import { useChatStore } from '@/stores/chat';
const chatStore = useChatStore()

</script>
<style scoped></style>