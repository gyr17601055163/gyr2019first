<template>
  <div>

    <!--新增试卷的管理总页面start-->

    <el-tabs type="border-card">
      <el-tab-pane label="新增题目">
        <newAddShiJuan></newAddShiJuan>
      </el-tab-pane>
      <el-tab-pane label="管理题目">
        <newManageShiJuan></newManageShiJuan>
      </el-tab-pane>
      <el-tab-pane label="批量导入题目">
        <newDaoRuShiJuan></newDaoRuShiJuan>
      </el-tab-pane>
    </el-tabs>

    <!--新增试卷的管理总页面end-->

  </div>
</template>

<script type="text/ecmascript-6">
  import newAddShiJuan from './newshiti/newAddShiJuan'
  import newDaoRuShiJuan from './newshiti/newDaoRuShiJuan'
  import newManageShiJuan from './newshiti/newManageShiJuan'

  export default {
    name: "newXinZengShiJuan",
    components: {
      newAddShiJuan,
      newDaoRuShiJuan,
      newManageShiJuan
    },
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
