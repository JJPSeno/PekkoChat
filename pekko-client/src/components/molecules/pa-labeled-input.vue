<template>
  <div class="col-wrapper">
  <b-label :labelFor="labeledInputId">
    <slot/>
  </b-label>
  <b-input
  :inputId="labeledInputId"
  :inputClass="labeledInputClass"
  :inputType="labeledInputType" 
  :inputPlaceholder="labeledInputPlaceholder"
  @input="updateValue"
  :value="modelValue"
  />
</div>
</template>
<script setup lang="ts">
import bInput from '../atoms/pa-input.vue';
import bLabel from '../atoms/pa-label.vue';
import { defineProps, defineEmits } from 'vue';

defineProps({
  labeledInputId: {
    type: String
  },
  labeledInputClass: {
    type: String
  },
  labeledInputType: {
    type: String,
    default: 'text',
      validator(value: string) {
        return ['text','file'].includes(value)
      }
  },
  labeledInputPlaceholder: {
    type: String
  },
  modelValue: {
    type: String,
  }
})

const emit = defineEmits(['update:modelValue'])

const updateValue = (e: Event) => {
  emit('update:modelValue', (e.target as HTMLInputElement).value)
};

</script>
<style scoped>
</style>