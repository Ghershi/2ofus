import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useJournalStore = defineStore('journal', () => {
    const entries = ref([])

    const fetchEntries = async (authHeader) => {
        try {
            const res = await fetch('http://localhost:8080/api/journal', {
                headers: {
                    ...authHeader
                }
            });
            if (!res.ok) throw new Error('Could not fetch entries');
            entries.value = await res.json();
        } catch (err) {
            console.error(err);
        }
    }

    const newMessageCount = (lastSeen) => {
        if (!lastSeen) return 0
        return entries.value.filter(e => new Date(e.timestamp) > new Date(lastSeen)).length
    }

    return { entries, fetchEntries, newMessageCount }
})