<template>
<textarea 
  :id="textAreaId"
  :placeholder="textAreaPlaceholder"
  @input="updateValue"
  :value="modelValue"
  :rows="textAreaRows" 
  :cols="textAreaCols">
<slot/>
</textarea>

</template>
<script setup lang="ts">
import { defineProps,defineEmits } from 'vue';

const props = defineProps({
    textAreaId: {
      type: String,
      required: true,
    },
    textAreaPlaceholder: {
      type: String,
      default: '',
    },
    modelValue: {
      type: String,
      required: true,
    },
    textAreaRows:{
      type: Number,
      default: 6
    },
    textAreaCols:{
      type: Number,
      default: 33
    }
})

const emit = defineEmits(['update:modelValue'])

const updateValue = (e: Event) => {
  emit('update:modelValue', (e.target as HTMLInputElement).value)
};

</script>
<style scoped>

</style>