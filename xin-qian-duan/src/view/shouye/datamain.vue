<template>
  <el-main id="et" :style="{height:wheight+'px','background-color':'#ffebcd'}">

      <router-view style="padding: 0px"></router-view>

  </el-main>

</template>

<script>
    export default {
        name: "datamain",
      data(){
          return{
            wheight:window.innerHeight-60
          }

      },
      mounted:function(){


      }
    }
</script>

<style scoped>

</style>
