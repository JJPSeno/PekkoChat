<template>
  <button 
    :class="btnClass"
    :type="btnType" 
    :name="btnName"
    @click="$emit('click', $event)"
  >
  <slot/>
  </button>
</template>
<script setup lang="ts">
const props = defineProps({
    btnClass: {
      type: String
    },
    btnName: {
      type: String
    },
    btnType: {
      type: String,
      required: true,
      default: 'button',
      validator(value: string) {
        return ['button','submit'].includes(value)
      }
    }
})

</script>
<style scoped>
  button{
    width: 200px;
    height: 40px;
    margin-top: 10px;
    align-self: flex-end;
  }
</style>