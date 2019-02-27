<template>
  <!--试卷管理start-->
  <div>

    <el-tabs type="border-card">
      <el-tab-pane label="创建试卷">
        <createShiJuan></createShiJuan>
      </el-tab-pane>
      <el-tab-pane label="快速创建试卷">
        <quickCreateShiJuan></quickCreateShiJuan>
      </el-tab-pane>
      <el-tab-pane label="试卷管理">
        <manageShiJuan></manageShiJuan>
      </el-tab-pane>
    </el-tabs>

  </div>
  <!--试卷管理end-->
</template>

<script type="text/ecmascript-6">
  import createShiJuan from './shijuan/createShiJuan';
  import quickCreateShiJuan from './shijuan/quickCreateShiJuan';
  import manageShiJuan from './shijuan/manageShiJuan';

  export default {
    name: "shiJuanGuanli",
    components: {
      createShiJuan,
      quickCreateShiJuan,
      manageShiJuan
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
