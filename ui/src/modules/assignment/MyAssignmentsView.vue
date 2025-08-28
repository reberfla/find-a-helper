<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import AssignmentCard from "@/components/assignment/AssignmentCard.vue";
import assignmentService from "@/service/AssignmentService.ts";
import type {AssignmentModel} from "@/models/AssignmentModel.ts";

const assignments = ref<AssignmentModel[]>([])

async function loadMyAssignments() {
  assignments.value = await assignmentService.getMyAssignment()
}
onMounted(loadMyAssignments)

</script>

<template>
  <div v-if="assignments.length === 0" class="no-offers-banner">
    <v-alert color="blue" type="info">
      Sie haben noch kein Angebot abgegeben.
    </v-alert>
  </div>

  <v-container :width="'100%'" class="py-0">
    <v-row class="row" dense>
      <v-col
        v-for="assignment in assignments"
        :key="assignment.id"
        class="d-flex justify-center">
        <AssignmentCard
          :assignment="assignment"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.row {
  display: flex;
  flex-direction: column;
}
</style>
