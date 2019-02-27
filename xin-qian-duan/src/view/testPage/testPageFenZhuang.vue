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
        <!--详细信息start-->
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>姓名: {{ scope.row.userName }}</p>
            <p>性别: {{ scope.row.sex }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.userName }}</el-tag>
            </div>
          </el-popover>
        </template>
        <!--详细信息end-->
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
    <pageFoot ref="pageFoot" @toforpage="toforpage"></pageFoot>
    <!--分页end-->


  </div>
</template>

<script type="text/ecmascript-6">

  //引入分页组件
  import pageFoot from '../common/pagefoot';

  export default {
    name: "testPageFenZhuang",
    components: {
      pageFoot
    },
    data() {
      return {
        tableData3: [],
        formInline: {},
        pageSize: 10,
        pageNo: 1
      }
    },
    methods: {
      toforpage(pageDate) {
        this.$data.pageSize = pageDate.pageSize;
        this.$data.pageNo = pageDate.pageNo;

        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageSize = this.$data.pageSize;
        pageInfo.pageNo = this.$data.pageNo;

        this.$axios.post(this.domain.serverpath + "user/testPage",JSON.stringify(pageInfo)).then((response) => {

          this.$data.tableData3 = response.data.result.resultList;
          this.$refs.pageFoot.total = response.data.result.totalCount;

        }).catch((error) => {

        })
      },
      onSubmit(){

        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageSize = this.$data.pageSize;
        pageInfo.pageNo = this.$data.pageNo;

        this.$axios.post(this.domain.serverpath + "user/testPage",JSON.stringify(pageInfo)).then((response) => {

          this.$data.tableData3 = response.data.result.resultList;
          this.$refs.pageFoot.total = response.data.result.totalCount;

        }).catch((error) => {

        })
      }
    },
    mounted() {
      this.$axios.post(this.domain.serverpath + "user/testPage").then((response) => {

        this.$data.tableData3 = response.data.result.resultList;
        this.$refs.pageFoot.total = response.data.result.totalCount;

      }).catch((error) => {

      })
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
