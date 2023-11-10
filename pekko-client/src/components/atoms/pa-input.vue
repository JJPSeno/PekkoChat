<template>

<input
    :id="inputId"
    :class="inputClass"
    :type="inputType" 
    :placeholder="inputPlaceholder"
    @input="updateValue"
    :value="modelValue"
    > 

</template>
<script setup lang="ts">
import { defineProps,defineEmits } from 'vue';

const props = defineProps({
    inputId: {
      type: String,
    },
    inputClass: {
      type: String,
    },
    modelValue: {
      type: String,
      required: true,
    },
    inputType:{
      type: String,
      default: 'text', //file
    },
    inputPlaceholder:{
      type: String,
      default: '',
    }
})

const emit = defineEmits(['update:modelValue'])

const updateValue = (e: Event) => {
  emit('update:modelValue', (e.target as HTMLInputElement).value)
};

</script>
<style scoped>

</style>