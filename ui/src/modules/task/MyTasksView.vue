<script setup lang="ts">
import TaskCard from '@/components/task/TaskCard.vue'
import taskService from '@/service/TaskService.ts'
import offerService from '@/service/OfferService.ts'
import {onMounted, ref, shallowRef} from 'vue'
import {getColorOfCategory, type Task, TaskStatus} from '@/models/TaskModel.ts'
import type {Offer} from '@/models/OfferModel.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'
import assignmentService from "@/service/AssignmentService.ts";
import OfferEditDialog from "@/components/offer/OfferEditDialog.vue";

const offersByTask = ref<Record<number, Offer[]>>({})
const tasks = ref<Task[]>([])
const selectedTask = ref<Task>(tasks.value[0])
const editDialog = shallowRef(false)
const showOfferDialog = shallowRef(false)
const selectedOffer = ref<Offer | null>(null)

function editTask(task: Task) {
  selectedTask.value = task
  editDialog.value = true
}

function openOfferReadonly(offer: Offer, task: Task) {
  selectedOffer.value = offer
  selectedTask.value = task
  showOfferDialog.value = true
}

function deleteTask(id: number) {
  taskService.deleteTask(id).then((t) => {
    tasks.value = tasks.value.filter((task) => task.id !== id)
  })
}

onMounted(() => {
  loadMyTasks()
})

function offersFor(taskId: number): Offer[] {
  return offersByTask.value[taskId] ?? []
}

async function loadMyTasks() {
  tasks.value = await taskService.getMyTasks().then((r)=>{
    return r.filter((t =>t.status === TaskStatus.OPEN ))
  })

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

function acceptOffer(offer: Offer) {
  //todo:implement
   assignmentService.createAssignment()
}

function rejectOffer(offer: Offer) {
  //todo:implement
  assignmentService.revertOffer()

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

              <v-expansion-panel-text :style="{padding:'6px'}">
                <template v-if="offersFor(task.id).length">
                  <div class="d-flex flex-column" style="gap:12px;">
                    <v-card
                      style="display: flex; flex-direction: column"
                      v-for="offer in offersFor(task.id)"
                      :key="offer.id"
                      variant="tonal"
                      class="pa-0"
                      @click="openOfferReadonly(offer, task)"
                    >
                      <div class="d-flex justify-space-between" style="gap:12px; flex-direction: column">
                        <div style="background-color: #ffffff; padding:6px;" clasS="flex-grow-1">
                          <div class="text-subtitle-2 font-weight-medium">
                            {{ offer.title || 'Angebot' }}
                          </div>
                          <div class="text-body-2 mt-1" style="white-space: normal; word-break: break-word;">
                            {{ offer.text }}
                          </div>
                        </div>
                      </div>
                      <div class="d-flex flex-row" style="width: 100%">
                        <v-btn
                          style="width:50%;border-bottom-right-radius: 0;border-top-right-radius: 0;border-top-left-radius: 0"
                          color="success"
                          variant="elevated"
                          size="small"
                          :disabled="offer.status === 'ACCEPTED'"
                          @click="acceptOffer(offer)"
                        >
                           Akzeptieren
                        </v-btn>
                        <v-btn
                          style="width:50%;border-bottom-left-radius: 0;border-top-right-radius: 0; border-top-left-radius: 0"
                          color="error"
                          variant="elevated"
                          size="small"
                          :disabled="offer.status === 'REJECTED'"
                          @click="rejectOffer(offer)"
                        >
                          Ablehnen
                        </v-btn>
                      </div>
                    </v-card>
                  </div>
                </template>

                <template v-else>
                  <v-alert type="info" variant="tonal" class="my-2">
                    Noch kein Angebot erhalten.
                  </v-alert>
                </template>
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

  <v-dialog v-model="showOfferDialog" max-width="700">
    <OfferEditDialog
      :readonly="true"
      v-if="selectedOffer"
      :offer="selectedOffer"
      @close="showOfferDialog = false"
    />
  </v-dialog>
</template>

<style scoped>
.task {
  width: 400px;
  margin: 10px;
}
.dialog {
  max-width: 1000px;
  width: 100%;
}
.task-wrapper { width: 420px; margin: 10px; }
</style>
