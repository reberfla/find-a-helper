<template>
 <div class="container" >
   <div class="header"></div> <!--here comes the header-component-->
   <div class="body">
     <div></div> <!--component for searching-->
     <div></div> <!--component for listview-->
   </div>
   <div class="footer" ></div> <!--here comes the footer-component-->
 </div>
</template>

<script setup lang="ts">
import {onBeforeMount, onBeforeUnmount, onMounted, onUnmounted} from "vue";

  onBeforeMount(()=>console.log("component will be mounted.."))
  onMounted(()=>console.log("component mounted... "))
  onBeforeUnmount(()=>console.log("component will be unmounted..."))
  onUnmounted(()=>console.log("component unmounted..."))

</script>

<style scoped>

  .container{
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .header{
    height: 6rem;
    background-color: #0e90d2;
  }

  .body{
    height: 85vh;
  }

  .footer{
    height: 3rem;
    background-color: #5bb75b;
  }
</style>
