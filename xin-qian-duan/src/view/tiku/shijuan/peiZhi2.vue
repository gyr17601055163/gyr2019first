<template>
  <div>

    <!--穿梭框start-->
    <el-transfer
      titles="试卷名称"
      style="width: 100%"
      v-model="value5"
      classname=""
      :props="{
      key: 'value',
      label: 'desc'
    }"
      :data="data3">
    </el-transfer>
    <!--穿梭框end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "peiZhi2",
    data() {
      return {
        data3: [],
        value5: []
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
