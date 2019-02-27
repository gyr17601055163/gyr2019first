<template>
  <div :style="{height:wheight+'px'}">
    <!--管理试卷页面start-->

    <!--模糊查寻表单start-->
    <likeFrom @likeFrom="likeFrom"></likeFrom>
    <!--模糊查寻表单end-->

    <!--数据表格start-->
    <el-table
      ref="multipleTable"
      :data="tableData3"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <!--复选框start-->
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <!--复选框end-->
      <el-table-column
        prop="tikuname"
        label="所属题库"
        width="120">
      </el-table-column>
      <el-table-column
        prop="tixingid"
        label="试卷类型"
        width="100">
      </el-table-column>
      <el-table-column
        prop="nanduid"
        label="试题难度"
        width="100">
      </el-table-column>
      <el-table-column
        prop="shitizhuangtai"
        label="试题状态"
        width="100">
      </el-table-column>
      <el-table-column
        prop="tigan"
        label="试题题干"
        width="260">
      </el-table-column>
      <el-table-column
        prop="userName"
        label="创建人"
        width="200">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="previewInfo(scope.row)"
          >预览
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
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--数据表格end-->

    <!--分页组件start-->
    <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
    <!--分页组件end-->


    <!--试卷预览start-->
    <!--预览试卷弹框start-->
    <el-dialog title="试卷预览" :visible.sync="dialogTableVisible">
        <yulan ref="yulandata" @yulanInfo="yulanInfo"></yulan>
    </el-dialog>
    <!--预览试卷弹框end-->
    <!--试卷预览end-->

    <!--修改弹框start-->
    <el-dialog center style="width: 1500px" title="修改试卷" :visible.sync="dialogTableVisible2">
      <newUpdateShiJuan ref="newUpdatexiugai" @newUpdateInfo="newUpdateInfo"></newUpdateShiJuan>
    </el-dialog>
    <!--修改弹框end-->


    <!--管理试卷页面end-->
  </div>
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../../common/pagefoot'
  import likeFrom from './likeFrom'
  import yulan from './yulan'
  import newUpdateShiJuan from './newUpdateShiJuan'

  export default {
    name: "newManageShiJuan",
    components: {
      pagefoot, //分页组件
      likeFrom,  //模糊查寻组
      yulan,
      newUpdateShiJuan
    },
    data() {
      return {
        wheight: window.innerHeight - 132,
        dialogTableVisible: false,
        dialogTableVisible2:false,
        pageSize: 10,
        pageNo: 1,
        tableData3: [],   //数据表格
        form:null,
        formInline:null
      }
    },
    methods: {
      yulanInfo(){
        this.$refs.yulandata.updateHuiXian(this.$data.form);
      },
      newUpdateInfo(){
        this.$refs.newUpdatexiugai.shujuhuixian(this.$data.formInline);
      },
      likeFrom(data){
        alert(JSON.stringify(data))
        //存放模糊分页参数
        let formInfo = {};
        formInfo = data;
        formInfo.pageNo = this.$data.pageNo;
        formInfo.pageSize = this.$data.pageSize;

        this.$axios.post(this.domain.serverpath+"tiku2/show",JSON.stringify(formInfo)).then((response)=>{
          this.$data.tableData3 = response.data.result.resultList;
          for(let i = 0; i < this.$data.tableData3.length; i++){
            this.$data.tableData3[i].userName = this.$data.tableData3[i].userName+" "+this.$data.tableData3[i].createtime;
          }
        }).catch((error)=>{
          this.$notify.error({
            title: '错误',
            message: '加载数据出现问题，请稍后重试'
          });
        })
      },
      toforpage(pageInfo) { //分页
        this.$data.pageSize = pageInfo.pageSize;
        this.$data.pageNo = pageInfo.pageNo;
        this.show();
      },
      previewInfo(row) {
        this.$data.dialogTableVisible=true;
        if (this.$data.form != null) {
          this.$refs.yulandata.updateHuiXian(row); //调用子组件中的回显方法，并在子组件页面初始化的时候，在钩子方法中初始化一下回显方法
        }
        this.$data.form = row
      },
      updateInfo(row) {
        this.$data.dialogTableVisible2 = true;
        if (this.$data.formInline != null) {
          this.$refs.newUpdatexiugai.shujuhuixian(row); //调用子组件中的回显方法，并在子组件页面初始化的时候，在钩子方法中初始化一下回显方法
        }
        this.$data.formInline = row;
      },
      show() { //从solr中加载试题
        //存放模糊分页参数
        let formInfo = {};
        formInfo.pageNo = this.$data.pageNo;
        formInfo.pageSize = this.$data.pageSize;

        this.$axios.post(this.domain.serverpath+"tiku2/show",JSON.stringify(formInfo)).then((response)=>{
          this.$data.tableData3 = response.data.result.resultList;
          for(let i = 0; i < this.$data.tableData3.length; i++){
            this.$data.tableData3[i].userName = this.$data.tableData3[i].userName+" "+this.$data.tableData3[i].createtime;
          }
        }).catch((error)=>{
          this.$notify.error({
            title: '错误',
            message: '加载数据出现问题，请稍后重试'
          });
        })
      }
    },
    mounted() {
      this.show();  //加载数据列表数据
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
