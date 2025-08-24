<script setup lang="ts">
import TaskCard from '@/components/task/TaskCard.vue'
import taskService from '@/service/TaskService.ts'
import offerService from '@/service/OfferService.ts'
import { onMounted, ref, shallowRef } from 'vue'
import type { Task } from '@/models/TaskModel.ts'
import type { Offer } from '@/models/OfferModel.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'

const offersByTask = ref<Record<number, Offer[]>>({})
const tasks = ref<Task[]>([])
const selectedTask = ref<Task>(tasks.value[0])
const editDialog = shallowRef(false)

function editTask(task: Task) {
  selectedTask.value = task
  editDialog.value = true
}

function deleteTask(id: number) {
  taskService.deleteTask(id).then(() => {
    tasks.value = tasks.value.filter((task) => task.id !== id)
  })
}

onMounted(() => {
  loadMyTasks()
})

function statusLabel(s: Offer['status'] | string) {
  switch (s) {
    case 'ACCEPTED': return 'Angenommen'
    case 'REJECTED': return 'Abgelehnt'
    default: return 'Eingereicht'
  }
}
function statusColor(s: Offer['status'] | string) {
  switch (s) {
    case 'ACCEPTED': return 'green'
    case 'REJECTED': return 'red'
    default: return 'blue'
  }
}
function activeColor(active?: boolean) {
  return active ? 'teal' : 'grey'
}

function offersFor(taskId: number): Offer[] {
  return offersByTask.value[taskId] ?? []
}

async function loadMyTasks() {
  tasks.value = await taskService.getMyTasks()

  const pairs = await Promise.all(
    tasks.value.map(async (t): Promise<[number, Offer[]]> => {
      try {
        const list = await offerService.getOffersForTask(t.id)
        return [t.id, list]
      } catch {
        return [t.id, [] as Offer[]]
      }
    })
  )

  const map: Record<number, Offer[]> = {}
  for (const [id, list] of pairs) map[id] = list as Offer[]
  offersByTask.value = map
}

</script>

<template>
  <v-dialog v-model="editDialog" class="dialog">
    <TaskEditDialog :task="selectedTask" @close="editDialog = false" :update="true" />
  </v-dialog>
  <div class="d-flex flex-wrap justify-space-evenly">
    <div v-for="task in tasks" :key="task.id" class="task-wrapper">
      <TaskCard
        class="task"
        :task="task"
        :private="true"
        @edit-task="editTask"
        @delete-task="deleteTask"
        :has-offer="offersFor(task.id).length > 0"
      >
        <template #offers>
          <v-expansion-panels variant="accordion" density="compact" class="mt-2">
            <v-expansion-panel>
              <v-expansion-panel-title>
                Erhaltene Angeboten
                <v-chip
                  size="x-small"
                  class="ml-2"
                  :color="offersFor(task.id).length ? 'primary' : 'grey'"
                  label
                >
                  {{ offersFor(task.id).length }}
                </v-chip>
              </v-expansion-panel-title>

              <v-expansion-panel-text>
                <v-list density="compact" class="offers-list">
                  <template v-if="offersFor(task.id).length">
                    <v-list-item
                      v-for="offer in offersFor(task.id)"
                      :key="offer.id"
                    >
                      <template #title>
                        <div class="d-flex justify-space-between align-center">
                          <div class="truncate">
                            <strong>{{ offer.title || 'Angebot' }}</strong>
                            <span class="ml-1 text-caption">
                              – {{ offer.text?.slice(0, 60) }}<span v-if="offer.text && offer.text.length > 60">…</span>
                            </span>
                          </div>
                          <div class="d-flex align-center" style="gap:6px;">
                            <v-chip size="x-small" :color="statusColor(offer.status)" class="text-white" label>
                              {{ statusLabel(offer.status) }}
                            </v-chip>
                            <v-chip size="x-small" :color="activeColor(offer.active)" class="text-white" label>
                              {{ offer.active ? 'Aktiv' : 'Inaktiv' }}
                            </v-chip>
                          </div>
                        </div>
                      </template>
                      <template #subtitle>
                        <span class="text-caption">
                          <strong>Gültig bis:</strong>
                          {{ offer.validUntil ? new Date(offer.validUntil + 'T00:00:00').toLocaleDateString() : '—' }}
                        </span>
                      </template>
                    </v-list-item>
                  </template>
                  <template v-else>
                    <v-list-item title="Noch kein Angebot erhalten" />
                  </template>
                </v-list>
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </template>
      </TaskCard>
    </div>
  </div>

  <div v-if="tasks.length === 0" class="no-offers-banner">
    <v-alert type="info" color="blue">
      Sie haben noch keinen Auftrag erstellt.
    </v-alert>
  </div>
</template>

<style scoped>
.task {
  width: 300px;
  margin: 10px;
}
.dialog {
  max-width: 1000px;
  width: 100%;
}
.task-wrapper { width: 320px; margin: 10px; }
.offers-list { border: 1px solid #eee; border-radius: 8px; }
.truncate { max-width: 65%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
</style>
