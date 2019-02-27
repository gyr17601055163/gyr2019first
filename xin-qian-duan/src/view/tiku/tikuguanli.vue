<template>
  <div>

    <!--标签页start-->
    <el-tabs type="border-card"  @tab-click="handleClick">
      <el-tab-pane label="新增题库">

        <!--新增题库start-->
        <xinzengtiku></xinzengtiku>
        <!--新增题库end-->

      </el-tab-pane>


      <el-tab-pane label="题库列表">

        <!--题库列表start-->
        <tikuliebiao></tikuliebiao>
        <!--题库列表end-->

      </el-tab-pane>
    </el-tabs>
    <!--标签页end-->

  </div>
</template>

<script type="text/ecmascript-6">
  import xinzengtiku from './xinzengtiku'
  import tikuliebiao from './tikuliebiao'

  export default {
    name: "tikuguanli",
    components:{
      xinzengtiku,
      tikuliebiao
    },
    methods:{
      handleClick(tab, event){
        if(tab.$options.propsData.label=="题库列表"){

        }
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
