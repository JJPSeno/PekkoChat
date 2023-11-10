<template>
  <form>
  <pa-labeled-input labeledInputId="registration-input-username" v-model="username">
  Username:
  </pa-labeled-input>
  <pa-labeled-input labeledInputId="registration-input-password" v-model="password">
  Password:
  </pa-labeled-input>
  <paButton btnClass="registration" btnName="registration" btnType="submit">Registration</paButton>
  </form>
</template>
<script setup lang="ts">
import paLabeledInput from '../molecules/pa-labeled-input.vue';
import paButton from '../atoms/pa-button.vue';

import { useChatStore } from '@/stores/chat';
const chatStore = useChatStore()

const username = chatStore.testRef
const password = chatStore.testRef

</script>
<style scoped></style>