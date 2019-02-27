<template>
  <!--试卷管理start-->
  <div :style="{height:wheight+'px'}">

    <!--模糊表单start-->
    <el-form :inline="true" :model="formInline" label-width="120px" class="demo-form-inline" size="mini">
      <el-form-item label="试卷名称：">
        <el-input v-model="formInline.tigan" placeholder=""></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuid" placeholder="试卷分类" style="width: 130px">
          <el-option label="" value=""></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuid" placeholder="试卷类型" style="width: 130px">
          <el-option label="" value=""></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuid" placeholder="试卷状态" style="width: 130px">
          <el-option label="" value=""></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <!--模糊表单end-->

    <!--数据表格start-->
    <el-table
      ref="multipleTable"
      :data="tableData3"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column
        prop="shijuanname"
        label="试卷名称"
        width="120">
      </el-table-column>
      <el-table-column
        prop="zhuangtai"
        label="试卷状态"
        width="120">
      </el-table-column>
      <el-table-column
        prop="starttime"
        label="时间设定"
        width="160">
      </el-table-column>
      <el-table-column
        prop="shijian"
        label="考试时长"
        width="120">
      </el-table-column>
      <el-table-column
        prop="shijuanleixing"
        label="试卷类型"
        width="120">
      </el-table-column>
      <el-table-column
        prop="zongfen"
        label="卷面总分"
        width="120">
      </el-table-column>
      <el-table-column
        prop="kaoshirenshu"
        label="考试人数"
        width="120">
      </el-table-column>
      <el-table-column
        prop="chuangjianren"
        label="创建人"
        width="120">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="peizhi(scope.row)"
          >配置
          </el-button>
          <el-button
            size="mini"
            type="primary"
            @click="updateInfo(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="danger"
          >考试详情
          </el-button>
          <el-button
            size="mini"
            type="danger"
          >更多
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--数据表格end-->

    <!--分页组件start-->
    <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
    <!--分页组件end-->


    <el-dialog
      title="配置试卷"
      width="1300px"
      :visible.sync="dialogTableVisible">
      <!--配置组件start-->
      <peiZhi ref="peiZhi" @peiZhi1="peiZhi1"></peiZhi>
      <!--配置组件end-->
    </el-dialog>


  </div>
  <!--试卷管理end-->
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../../common/pagefoot'
  import peiZhi from './peiZhi'
  export default {
    name: "manageShiJuan",
    components:{
      pagefoot,
      peiZhi,
    },
    data() {
      return {
        wheight: window.innerHeight - 132,
        dialogTableVisible:false,
        pageSize:10,
        pageNo:1,
        tableData3:[],
        formInline:{},
        rowData:null
      }
    },
    methods: {
      peiZhi1(){
        this.$refs.peiZhi.shujuhuixian(this.$data.rowData);
      },
      peizhi(row){  //配置按钮
        this.$data.dialogTableVisible = true;

        if (this.$data.rowData != null) {
          this.$refs.peiZhi.shujuhuixian(row); //调用子组件中的回显方法，并在子组件页面初始化的时候，在钩子方法中初始化一下回显方法
        }
        this.$data.rowData = row;

        //this.$refs.peiZhi.rowdata = JSON.stringify(row);
        //this.$router.push("/datamain/yewu/peiZhi2");  //跳转到配置页
      },
      updateInfo(row){

      },
      onSubmit(){

      },
      toforpage(data){
        this.$data.pageSize = data.pageSize;
        this.$data.pageNo = data.pageNo;
        this.showList();
      },
      showList(){
        let pageInfo = {};
        pageInfo.pageSize = this.$data.pageSize;
        pageInfo.pageNo = this.$data.pageNo;
        let qs = require("qs");
        this.$axios.post(this.domain.serverpath+"tiku2/manageKaoShiShiJuan",qs.stringify(pageInfo)).then(response=>{
          this.$data.tableData3 = response.data.result.resultList;
          this.$refs.pagefoot.total = response.data.result.totalCount;
          this.$data.tableData3.forEach(x=>{
            x.starttime = x.starttime +" "+x.endtime;
            x.zhuangtai = this.domain.zhuangtai[x.zhuangtai].label;
            x.shijuanleixing = this.domain.shijuanleixing[x.shijuanleixing].label;
            x.chuangjianren = x.chuangjianren +" "+x.createtime;
          })
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
