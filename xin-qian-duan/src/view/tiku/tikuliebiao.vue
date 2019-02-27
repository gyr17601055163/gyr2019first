<template>
  <div :style="{height:wheight+'px'}"
    v-loading="loading2"
    element-loading-text="玩命处理中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  >

    <!--模糊表单start-->
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="题库名称">
        <el-input v-model="formInline.tikuname" placeholder="题库名称"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="formInline.tikuzhuangtai" placeholder="状态">
          <el-option label="开放" value="1"></el-option>
          <el-option label="关闭" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <!--模糊表单end-->

    <!--题库列表表格start-->
    <el-table
      :data="tableData"

      style="width: 100%">
      <!--v-if="show": 当show在data块中值为false时，可以隐藏该列-->
      <el-table-column
        v-if="show"
        prop="id"
        label="ID"
        width="180">
      </el-table-column>
      <el-table-column
        prop="tikuname"
        label="题库名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="tikuzhuangtai"
        label="状态"
        width="180">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini" @click="shujufenxi(scope.row)">数据分析
          </el-button>
          <el-button
            size="mini" @click="update(scope.row)">修改
          </el-button>
          <el-button
            size="mini" @click="exportTiKu(scope.row)">题库导出
          </el-button>
          <el-button
            size="mini"
            type="danger" @click="shanchu(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--题库列表表格end-->

    <!--分页插件start-->
    <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
    <!--分页插件end-->

    <!--Dialog弹出框start-->
    <el-dialog
      title="数据分析"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose">

      <!--饼图start-->
      <tikufenxi ref="tikufenxi"></tikufenxi>
      <!--饼图end-->

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary">确 定</el-button>
      </span>
    </el-dialog>
    <!--Dialog弹出框end-->


    <!--题库修改弹出框start-->
    <el-dialog
      title="修改题库信息"
      :visible.sync="dialogVisible2"
      width="50%"
      :before-close="handleClose2">

      <!--修改表单start-->
      <tikuxiugai ref="tikuxiugai1" @tikuxiugai9="tikuxiugai9"></tikuxiugai>
      <!--修改表单end-->

    </el-dialog>
    <!--题库修改弹出框end-->

  </div>
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../common/pagefoot'
  import tikufenxi from './tikufenxi'
  import tikuxiugai from './tikuxiugai'

  export default {
    name: "tikuliebiao",
    components: {
      pagefoot,
      tikufenxi,
      tikuxiugai
    },
    data() {
      return {
        wheight: window.innerHeight - 132,
        cruurow: null,
        pageSize: 10,
        pageNo: '',
        dialogVisible: false,
        dialogVisible2: false,
        loading2: false,
        show: false,   // 隐藏id列
        tableData: [],
        formInline: {
          tikuname: '',
          tikuzhuangtai: ''
        }
      }
    },
    methods: {
      exportTiKu(row){ //题库导出
        //alert(JSON.stringify(row.id))
        window.location.href = this.domain.serverpath+"tiku2/exportExcelData?tikuid="+JSON.stringify(row.id);
      },
      tikuxiugai9(xiaoguo) {
        this.$refs.tikuxiugai1.updatehuixian(this.$data.cruurow);
        this.$data.dialogVisible2 = xiaoguo;     //用于当修改成功之后关闭修改窗口
      },
      toforpage(pageInfo) {
        this.$data.pageSize = pageInfo.pageSize;
        this.$data.pageNo = pageInfo.pageNo;
        pageInfo.tikuname = this.$data.formInline.tikuname;
        pageInfo.tikuzhuangtai = this.$data.formInline.tikuzhuangtai

        this.$axios.post(this.domain.serverpath + "tiku/tiKuLieBiao", JSON.stringify(pageInfo)).then((response) => {

          this.$data.tableData = response.data.result.resultList;
          this.$refs.pagefoot.total = response.data.result.totalCount;

        }).catch((error) => {

        })
      },
      onSubmit() {
        let pageInfo = this.$data.formInline;

        this.$axios.post(this.domain.serverpath + "tiku/tiKuLieBiao", JSON.stringify(pageInfo)).then((response) => {

          this.$data.tableData = response.data.result.resultList;
          this.$refs.pagefoot.total = response.data.result.totalCount;

        }).catch((error) => {

        })
      },
      shujufenxi(row) {   //数据分析按钮
        this.$data.dialogVisible = true;  //打开弹窗
      },
      handleClose(done) {
        this.$data.dialogVisible = false;
      },
      handleClose2(done) {
        this.$data.dialogVisible2 = false;
      },
      update(row) {  //修改按钮
        if (this.$data.cruurow != null) {
          this.$refs.tikuxiugai1.updatehuixian(row) //调用子组件中的回显方法，并在子组件页面初始化的时候，在钩子方法中初始化一下回显方法
        }
        this.$data.cruurow = row
        this.$data.dialogVisible2 = true;
      },
      shanchu(row) {
        let qs = require("qs");
        this.$axios.post(this.domain.serverpath + "tiku/shanchutiku", qs.stringify(row)).then((response) => {

          let pageInfo = {};
          pageInfo.pageNo = this.$refs.pagefoot.pageNo;
          pageInfo.pageSize = this.$refs.pagefoot.pageSize;

          this.$axios.post(this.domain.serverpath + "tiku/tiKuLieBiao", JSON.stringify(pageInfo)).then((response) => {

            this.$data.tableData = response.data.result.resultList;
            this.$refs.pagefoot.total = response.data.result.totalCount;

          }).catch((error) => {

          })

          this.$message({
            message: '恭喜你，删除成功',
            duration: 1500,
            type: 'success'
          });
        }).catch((error) => {
          this.$message({
            message: '删除失败',
            duration: 1500,
            type: 'error'
          });
        })
      }
    },
    mounted() {
      let pageInfo = {};
      pageInfo.pageNo = this.$refs.pagefoot.pageNo;
      pageInfo.pageSize = this.$refs.pagefoot.pageSize;

      this.$axios.post(this.domain.serverpath + "tiku/tiKuLieBiao", JSON.stringify(pageInfo)).then((response) => {

        this.$data.tableData = response.data.result.resultList;
        this.$refs.pagefoot.total = response.data.result.totalCount;

      }).catch((error) => {

      })
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
