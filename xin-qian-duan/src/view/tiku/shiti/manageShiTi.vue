<template>
  <div>

    <!--模糊表单start-->
    <el-form :inline="true" :model="formInline" label-width="70px" class="demo-form-inline" size="mini">
      <el-form-item label="关键词：">
        <el-input v-model="formInline.tigan" placeholder="试题题干查寻"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuid" placeholder="所属题库" style="width: 130px">
          <el-option v-for="items in tikus" key="tikus" :label="items.tikuname" :value="items.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tixingid" placeholder="试题类型" style="width: 130px">
          <el-option v-for="item in shiTiLeiXing" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.nanduid" placeholder="试题难度" style="width: 130px">
          <el-option v-for="(item,index) in shiTiNanDu" key="shiTiNanDu" :label="item" :value="index+1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.shitizhuangtai" placeholder="试题状态" style="width: 130px">
          <el-option v-for="item in shitizhuangtai" key="shitizhuangtai" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuzhuangtai" placeholder="排序方式" style="width: 130px">
          <el-option label="更新中..." value="1"></el-option>
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


    <!--导入分页组件start-->
    <pagefoot ref="pagefoot" @toforpage="toforpage"></pagefoot>
    <!--导入分页组件end-->


    <!--预览试卷弹框start-->
    <el-dialog title="试卷预览" :visible.sync="dialogTableVisible">
      <el-form :model="form">
        <el-form-item label="试题类型：">{{form.tixingid}}</el-form-item>
        <el-form-item label="试题难度：">{{form.nanduid}}</el-form-item>
        <el-form-item label="所属题库：">{{form.tikuname}}</el-form-item>
        <el-form-item label="试题状态：">{{form.shitizhuangtai}}</el-form-item>
        <el-form-item label="试题题干：">{{form.tigan}}</el-form-item>
        <el-form-item label="选项设置：">{{form.xuanxiang}}--{{form.xuanxiangmiaoshu}}</el-form-item>
        <el-form-item label="试题答案：">{{form.daan}}</el-form-item>
        <el-form-item label="答案解析：">{{form.fenxi}}</el-form-item>
      </el-form>
    </el-dialog>
    <!--预览试卷弹框end-->


    <!--修改试卷信息弹框start-->
    <el-dialog title="试卷预览" :visible.sync="dialogTableVisible2">
      <updateShiTi></updateShiTi>
    </el-dialog>
    <!--修改试卷信息弹框end-->



  </div>
</template>

<script type="text/ecmascript-6">
  import pagefoot from '../../common/pagefoot'
  import updateShiTi from './updateShiTi'

  export default {
    name: "manageShiTi",
    components: {
      pagefoot,
      updateShiTi
    },
    data() {
      return {
        pageNo: 1,
        pageSize: 10,
        dialogTableVisible: false,
        dialogTableVisible2: false,
        shiTiNanDu: this.domain.shiTiNanDu,
        shiTiLeiXing: this.domain.shiTiLeiXing,
        shitizhuangtai: this.domain.shitizhuangtai,
        tableData3: [],
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        tikus: [],
        checkList: [],       //选中的答案
        xuanxiang: this.domain.xuanxiang,       //给复选框提供的所有的选项
        fuxuankuanglist: ['A', 'B', 'C', 'D'],  //每个复选框的值
        textareaValues: ['', '', '', ''],        //每个文本框的内容
        tikus: [],
        formInline: {},
        form: {}
      }
    },
    methods: {
      previewInfo(row) {
        this.$data.dialogTableVisible = true;   //打开预览弹框
        //alert(JSON.stringify(row))
        this.$data.form = row;
      },
      updateInfo(row) { //修改按钮
        this.$data.dialogTableVisible2 = true;   //打开修改弹框
        this.$data.updateformInline = row;
        alert(JSON.stringify(this.$data.updateformInline.nanduid))
      },
      onSubmit() { //模糊查寻
        this.doShow();
      },
      getTiKuInfoForRedis() {  //从Redis中获取题库数据
        this.$axios.post(this.domain.serverpath + "tiku/getTiKuInfoForRedis").then((response) => {
          //请求成功，给表单中的下拉菜单赋值
          this.$data.tikus = response.data.result;
        }).catch((error) => {

        })
      },
      toforpage(pageInfo) {
        this.$data.pageNo = pageInfo.pageNo;
        this.$data.pageSize = pageInfo.pageSize;
        this.doShow();
      },
      doShow() {
        //加载数据列表start
        let pageInfo = {};
        pageInfo = this.$data.formInline;
        pageInfo.pageNo = this.$data.pageNo;
        pageInfo.pageSize = this.$data.pageSize;

        this.$axios.post(this.domain.serverpath + "tiku/shiJuanLeiBiao", JSON.stringify(pageInfo)).then((response) => {
          this.$refs.pagefoot.total = response.data.result.totalCount;
          this.$data.tableData3 = response.data.result.resultList;

          for (let i = 0; i < this.$data.tableData3.length; i++) {
            //转换试卷类型的格式
            this.$data.tableData3[i].tixingid = this.domain.shiTiLeiXing[this.$data.tableData3[i].tixingid - 1].label;
            //转换试题难度的格式
            this.$data.tableData3[i].nanduid = this.domain.shiTiNanDu[this.$data.tableData3[i].nanduid - 1];
            //转换试题状态的格式
            if (this.$data.tableData3[i].shitizhuangtai == "0") {
              this.$data.tableData3[i].shitizhuangtai = this.domain.shitizhuangtai[0].label;
            } else {
              this.$data.tableData3[i].shitizhuangtai = this.domain.shitizhuangtai[1].label;
            }
            //转换创建人的格式
            this.$data.tableData3[i].userName = this.$data.tableData3[i].userName + " " + this.$data.tableData3[i].createtime;
          }

        }).catch((erroe) => {


        })
        //加载数据列表end
      }
    },
    mounted() {
      this.doShow();
      this.getTiKuInfoForRedis();

    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
