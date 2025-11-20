<template>
    <div class="calendar">
      <div class="calendar-header">
        <button @click="prevMonth">←</button>
        <div>{{ monthYear }}</div>
        <button @click="nextMonth">→</button>
      </div>
  
      <div class="day-names">
        <div v-for="day in days" :key="day">{{ day }}</div>
      </div>
  
      <div class="calendar-days">
        <div v-for="n in firstDay" :key="'blank-' + n"></div>
        <div
          v-for="d in lastDate"
          :key="d"
          :class="{ today: isToday(d), selected: isSelected(d) }"
          @click="selectDate(d)"
        >
          {{ d }}
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  
  const emit = defineEmits(['select-date'])
  
  const days = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa']
  const currentDate = ref(new Date())
  const selectedDate = ref(null)
  
  const year = computed(() => currentDate.value.getFullYear())
  const month = computed(() => currentDate.value.getMonth())


  const monthYear = computed(() =>
    currentDate.value.toLocaleString('default', { month: 'long', year: 'numeric' })
  )
  
  const firstDay = computed(() => new Date(year.value, month.value, 1).getDay())
  const lastDate = computed(() => new Date(year.value, month.value + 1, 0).getDate())
  
  function prevMonth() {
    currentDate.value.setMonth(currentDate.value.getMonth() - 1)
    currentDate.value = new Date(currentDate.value)
  }
  
  function nextMonth() {
    currentDate.value.setMonth(currentDate.value.getMonth() + 1)
    currentDate.value = new Date(currentDate.value)
  }
  
  function isToday(day) {
    const today = new Date()
    return (
      today.getDate() === day &&
      today.getMonth() === month.value &&
      today.getFullYear() === year.value
    )
  }
  
  function isSelected(day) {
    return (
      selectedDate.value &&
      selectedDate.value.getDate() === day &&
      selectedDate.value.getMonth() === month.value &&
      selectedDate.value.getFullYear() === year.value
    )
  }
  
  function selectDate(day) {
    selectedDate.value = new Date(year.value, month.value, day)
    emit('select-date', selectedDate.value)
  }
  </script>
  
  <style scoped>
  .calendar {
  width: 320px;
  background-color: #fdfaf7;
  border-radius: 16px;
  padding: 1rem;
  box-shadow: 0 0 0 2px #7e6aa8;
  color: #4f4289;
}


  
  .calendar-header {
    background-color: #e9d8fd;
    border-radius: 12px;
    padding: 0.5rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    font-size: 1.2rem;
  }
  
  .calendar-header button {
    background: none;
    border: none;
    font-size: 1.2rem;
    cursor: pointer;
    color: #4f4289;
  }
  
  .day-names,
  .calendar-days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 0.3rem;
    text-align: center;
    margin-top: 1rem;
  }
  
  .day-names div {
    font-weight: bold;
  }
  
  .calendar-days div {
    padding: 0.5rem;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
  }
  
  .calendar-days .today {
    background-color: #cbd5ff;
    font-weight: bold;
  }
  
  .calendar-days .selected {
    background-color: #d8b4fe;
    font-weight: bold;
  }
  :host {
  display: block;
  text-align: right;
}

.calendar {
  margin-left: 0;
  margin-right: auto;
}
@media (max-width: 768px) {
  .calendar {
    margin: 0 auto;
  }
}


  </style>
  