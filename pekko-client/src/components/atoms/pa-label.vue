<template>
<label :class="labelClass" :for="labelFor">
  <slot/>
</label>
</template>
<script setup lang="ts">
const props = defineProps({
    labelFor: {
      type: String
    },
    labelClass: {
      type: String
    },
})
</script>
<style scoped>

</style>