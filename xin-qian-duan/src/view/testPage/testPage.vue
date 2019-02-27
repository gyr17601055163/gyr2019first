<template>
  <div>
    <br>

    <!--模糊start-->
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="用户名">
        <el-input v-model="formInline.userName" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="formInline.sex" placeholder="性别">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <!--模糊end-->

    <!--列表start-->
    <el-table
      :data="tableData3"
      height="480"
      border
      style="width: 100%">
      <el-table-column
        prop="userName"
        label="用户名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="loginName"
        label="登录名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="sex"
        label="性别">
      </el-table-column>
    </el-table>
    <!--列表end-->

    <!--分页start-->
    <el-pagination
      background
      layout="sizes,prev, pager, next"
      :page-sizes="pageSizes"
      :page-size="pageSize"
      @current-change="urrentChange"
      @size-change="sizeChange"
      :total="totalCount">
    </el-pagination>
    <!--分页end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "testPage",
    data() {
      return {
        pageSizes: [10, 15, 20, 25, 30],
        pageSize: 10,
        pageNo: 1,
        totalCount: 0,
        tableData3: [{
          userName: '',
          loginName: '',
          sex: ''
        }],
        formInline:{
          userName: '',
          sex: ''
        }
      }
    },
    methods: {
      urrentChange(pageNo) {
        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageNo = pageNo;
        this.$data.pageNo = pageNo;
        pageInfo.pageSize = this.$data.pageSize;

        this.$axios.post(this.domain.serverpath + "user/testPage", JSON.stringify(pageInfo)).then((response) => {
          //给列表赋值
          this.$data.tableData3 = response.data.result.resultList;
          //给总页面赋值
          this.$data.totalCount = response.data.result.totalCount;

        }).catch((error) => {

        })
      },
      sizeChange(pageSize) {
        this.$data.pageSize = pageSize;

        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageSize = pageSize
        pageInfo.pageNo = 1

        this.$axios.post(this.domain.serverpath + "user/testPage",JSON.stringify(pageInfo)).then((response) => {
          //给列表赋值
          this.$data.tableData3 = response.data.result.resultList;
          //给总页面赋值
          this.$data.totalCount = response.data.result.totalCount;

        }).catch((error) => {

        })
      },
      onSubmit(){
        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageSize = this.$data.pageSize;
        pageInfo.pageNo = this.$data.pageNo;

        this.$axios.post(this.domain.serverpath + "user/testPage",JSON.stringify(pageInfo)).then((response) => {
          //给列表赋值
          this.$data.tableData3 = response.data.result.resultList;
          //给总页面赋值
          this.$data.totalCount = response.data.result.totalCount;

        }).catch((error) => {

        })
      }
    },
    mounted() {  //在页面一加载的时候，去访问后台数据库获取列表数据
      this.$axios.post(this.domain.serverpath + "user/testPage").then((response) => {
        //给列表赋值
        this.$data.tableData3 = response.data.result.resultList;
        //给总页面赋值
        this.$data.totalCount = response.data.result.totalCount;

      }).catch((error) => {

      })
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
