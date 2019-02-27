<template>
  <div>
    <!--<el-row type="flex" class="row-bg">
      <el-col :span="24">
        <div class="grid-content bg-purple-light1">&nbsp;&nbsp;&nbsp;&nbsp;<span class="yangshi">首页</span>&nbsp; > &nbsp;新增试题
        </div>
        <div class="header-yangshi">
          <span>&nbsp;&nbsp;&nbsp;新增试题</span>
        </div>
      </el-col>
    </el-row>-->


    <!--标签页start-->
    <el-tabs type="border-card">
      <el-tab-pane label="新增试卷">

        <!--新增试卷start-->
        <addShiTi></addShiTi>
        <!--新增试卷end-->

      </el-tab-pane>
      <el-tab-pane label="管理试题">

        <!--管理试题start-->
        <manageShiTi></manageShiTi>
        <!--管理试题end-->

      </el-tab-pane>
      <el-tab-pane label="批量导入试题">

        <!--批量导入试题start-->
        <importShiTis></importShiTis>
        <!--批量导入试题end-->

      </el-tab-pane>
    </el-tabs>
    <!--标签页end-->

  </div>
</template>

<script type="text/ecmascript-6">
  import addShiTi from './shiti/addShiTi'
  import manageShiTi from './shiti/manageShiTi'
  import importShiTis from './shiti/importShiTis'

  export default {
    name: "xinzengshijuan",
    components:{
      addShiTi,
      manageShiTi,
      importShiTis
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .el-row {
    margin-bottom: 0px;
    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-col {
    border-radius: 4px;
  }

  .bg-purple-dark {
    background: #99a9bf;
  }

  .bg-purple {
    background: #d3dce6;
  }

  .bg-purple-light1 {
    background: #e5e9f2;
    text-align: left
  }

  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    line-height: 36px
    text-align: left
    font-size: 14px
  }

  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .yangshi {
    color: #0000ff
  }
  .header-yangshi{
    height:40px;
    line-height: 50px
    font-size :18px
    text-align: left
  }
</style>
