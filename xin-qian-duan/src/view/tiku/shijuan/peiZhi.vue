<template>
  <div>

    <!--左边start-->
    <div style="height: 500px">
      <div style="float: left;width: 60%">
        <div>{{rowdata.shijuanname}}</div>
        <div>{{rowdata.shijuanleixing}}</div>
        <div>时间设定：{{rowdata.starttime}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;考试时长：{{rowdata.shijian}}分钟</div>
        <div>卷面部分：{{rowdata.zongfen}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;及格分数：{{rowdata.jihefenshu}}</div>
        <el-button size="mini">增加章节</el-button>
        <el-button size="mini" type="primary">全部展开</el-button>
        <el-button size="mini">全部收缩</el-button>
        <el-button size="mini">计算总分</el-button>


      </div>
      <!--左边end-->

      <!--右边start-->
      <div style="float: left;width: 40%">
        <div>从题库中筛选试题加入到当前试卷</div>
        <!--模糊表单start-->
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
          <el-form-item label="">
            <el-select v-model="formInline.tikuid" placeholder="试题类型" style="width: 100px">
              <el-option v-for="(item,index) in shiTiLeiXing" :key="index" :label="item.label" :value="item.label"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-select v-model="formInline.tikuid" placeholder="所属题库" style="width: 140px">
              <el-option v-for="(item,index) in tikus" :key="index" :label="item.tikuname" :value="item.tikuname"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-select v-model="formInline.tikuid" placeholder="试题难度" style="width: 100px">
              <el-option v-for="(item,index) in shiTiNanDu" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-input v-model="formInline.tigan" placeholder="关键词" style="width: 80px"></el-input>
          </el-form-item>
          <el-form-item>
            <i class="el-icon-search"></i>
          </el-form-item>
        </el-form>
        <!--模糊表单end-->

        <div style="border:1px solid #e5e5e5;height: 450px">
          <!--数据表格start-->
          <el-table
            ref="multipleTable"
            :data="tableData3"
            tooltip-effect="dark"
            style="width: 100%;"
            @row-click="rowClick"
          >
            <el-table-column
              prop="tigan"
              label="题目名称">
            </el-table-column>
          </el-table>
          <!--数据表格end-->

          <!--分页组件start-->
          <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
          <!--分页组件end-->
        </div>

      </div>
      <!--右边end-->

    </div>


  </div>
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../../common/pagefoot'
  export default {
    name: "peiZhi",
    components:{
      pagefoot
    },
    data() {
      return {
        pageSize:10,
        pageNo:1,
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        shiTiNanDu: this.domain.shiTiNanDu,        //试题难度
        formInline:{},
        tikus: [],  //题库
        tableData3:[],
        rowdata:{}
      }
    },
    methods:{
      rowClick(){
        alert("00")
      },
      shujuhuixian(row){
        this.$data.rowdata = row;
      },
      toforpage(pageInfo) { //分页
        this.$data.pageSize = pageInfo.pageSize;
        this.$data.pageNo = pageInfo.pageNo;
        this.getTuMus();
      },
      getTikusToRedis() {  //从Redis中获取题库信息
        this.$axios.post(this.domain.serverpath + "tiku2/getTiKusToRedis").then((response) => {
          this.$data.tikus = response.data.result
        }).catch((error) => {
          this.$notify({
            title: '警告',
            message: '没有获取到可用的题库信息！',
            type: 'warning'
          });
        })
      },
      //从Solr中加载所有的题干
      getTuMus(){
        //存放模糊分页参数
        let formInfo = {};
        formInfo.pageNo = this.$data.pageNo;
        formInfo.pageSize = this.$data.pageSize;

        this.$axios.post(this.domain.serverpath+"tiku2/show",JSON.stringify(formInfo)).then((response)=>{
          this.$data.tableData3 = response.data.result.resultList;
          this.$refs.pagefoot.total = response.data.result.totalCount;
        }).catch((error)=>{
          this.$notify.error({
            title: '错误',
            message: '加载数据出现问题，请稍后重试'
          });
        })
      }
    },
    mounted(){
      this.getTuMus();
      this.$emit("peiZhi1");
      this.getTikusToRedis(); //加载Redis中的题库
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
