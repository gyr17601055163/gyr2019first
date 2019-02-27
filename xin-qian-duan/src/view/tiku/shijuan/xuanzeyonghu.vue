<template>
  <div>


    <!--form模糊表单start-->

    <!--form模糊表单end-->


    <!--数据表格start-->
    <el-table
      v-loading="loading"
      height="450"
      element-loading-text="拼命加载中>_<"
      :data="tableData"
      @select="checkedRow"
      style="width: 100%">
      <el-table-column
        type="selection"
        prop="id"
        width="55">
      </el-table-column>
      <el-table-column
        prop="id"
        width="150"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="loginName"
        width="120"
        label="用户名">
      </el-table-column>
      <el-table-column
        prop="userName"
        sortable
        width="120"
        label="真实姓名">
      </el-table-column>
      <el-table-column
        prop="idcard"
        width="180"
        label="证件编号">
      </el-table-column>
      <el-table-column
        prop="buMen"
        width="150"
        label="所属部门">
      </el-table-column>
      <el-table-column
        prop="yonghuzhuangtai"
        label="用户状态">
      </el-table-column>
      <el-table-column
        prop="logintime"
        width="130"
        label="最后登录时间">
      </el-table-column>
      <el-table-column
        prop="address"
        label="操作">

        <template slot-scope="scope">   <!--slot-scope="scope" 获取到该行表格的所有数据-->
          <el-button
            size="mini"
            @click="deleteRole(scope.row)">选择
          </el-button>
        </template>

      </el-table-column>
    </el-table>
    <!--数据表格end-->

    <!--分页组件start-->
    <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
    <!--分页组件end-->

  </div>
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../../common/pagefoot'

  export default {
    name: "xuanzeyonghu",
    components:{
      pagefoot
    },
    data() {
      return {
        loading: false,
        pageSize:10,
        pageNo:1,
        totalCount:0,
        tableData: [],
        checkedInfo:[]
      }
    },
    methods: {
      deleteRole(){

      },
      checkedRow(row){
        /*for (let i = 0; i < row.length; i++) {
          if(this.$data.checkedInfo.indexOf(row[i].id) == -1){  //说明选中的那一行在这个数据中不存在
            this.$data.checkedInfo.push(row[i].id);   //添加
            break;
          }
        }*/
        let checkedUserInfo = {};
        row.forEach((r)=>{
          checkedUserInfo={id:r.id,userName:r.userName};
        })
        this.$data.checkedInfo.push(checkedUserInfo)
      },
      toforpage(pageInfo){
        this.$data.pageSize = pageInfo.pageSize;
        this.$data.pageNo = pageInfo.pageNo;
        this.showList();
      },
      showList(){
        let data = {};
        data.pageSize = this.$data.pageSize;
        data.pageNo = this.$data.pageNo;
        // let qs = require("qs");
        this.$axios.post(this.domain.serverpath + "user/getUserPageList",JSON.stringify(data)).then((response) => {
          //alert(JSON.stringify(response.data.result.userPage.resultList))
          this.$data.tableData = response.data.result.userPage.resultList;
          this.$refs.pagefoot.total = response.data.result.userPage.totalCount;
          this.$data.totalCount = response.data.result.userPage.totalCount;
        }).catch((error) => {

        })
      }
    },
    mounted() {
      this.showList();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
