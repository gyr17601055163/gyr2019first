<template>
  <div
    v-loading="loading2"
    element-loading-text="玩命处理中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  >

    <el-form :model="formInline" class="demo-form-inline">
      <el-form-item label="试题类型：">
        <el-select v-model="formInline.tixingid" placeholder="多选题">
          <el-option v-for="item in shiTiLeiXing" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题难度：">
        <el-rate style="margin-top: 12px" v-model="formInline.nanduid" show-text :texts="shiTiNanDu"></el-rate>
      </el-form-item>

      <el-form-item label="所属题库：">
        <el-select v-model="formInline.tikuid" placeholder="所属题库">
          <el-option v-for="(items,index) in tikus" :label="items.tikuname" :key="index" :value="items.id"></el-option>
        </el-select>
      </el-form-item>



      <el-form-item label="试题来源：">
        <el-input style="width: 25.5%" v-model="formInline.laiyuan" placeholder="原创"></el-input>
      </el-form-item>

      <el-form-item label="试题状态：">
        <el-select v-model="formInline.shitizhuangtai" placeholder="开放">
          <el-option label="开放" value="1"></el-option>
          <el-option label="关闭" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题题干：">
        <div style="float: left;width: 200px"></div>
        <div style="float: left">
          <!--富文本编辑器1 start-->
          <script id="editor11" type="text/plain"></script>
          <!--富文本编辑器1 end-->
        </div>
      </el-form-item>

      <el-form-item label="试题设置：">
        <div style="float: left;width: 200px"></div>
        <div style="float: left">
          <el-button type="info" plain @click="addXuanXiang" size="small">增加选项</el-button>
          <br>
          <!-- el-row:在该标签中的东西可以在一行显示 -->
          <el-checkbox-group v-model="checkList">
            <el-row v-for="(item,index) in fuxuankuanglist" :key="index" :style="{'margin-bottom':5+'px'}">
              <el-checkbox :label="fuxuankuanglist[index]" :style="{'margin-right':10+'px'}"></el-checkbox>
              <el-input type="textarea" style="width: 400px" autosize placeholder="请输入内容"
                        v-model="textareaValues[index]" :style="{'margin-bottom':8+'px'}"></el-input>
              <el-button type="danger" icon="el-icon-delete" circle @click="deleteXuanXiang(index)"
                         :style="{'margin-left':10+'px'}"></el-button>
            </el-row>
          </el-checkbox-group>


        </div>
      </el-form-item>

      <el-form-item label="试题分析：">
        <div style="float: left;width: 200px"></div>
        <div style="float: left">
          <!--富文本编辑器1 start-->
          <script id="editor22" type="text/plain"></script>
          <!--富文本编辑器1 end-->
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>


    <!--++++++++++++++++++++++++++++++++++++++++++++++++++++++测试富文本编辑器和复选框添加删除+++++++++++++++++++++++++++++++++++++++++++++++++++++++-->
    <!--<el-button @click="getDatas">获取富文本编辑器的内容</el-button>

    &lt;!&ndash;富文本编辑器1 start&ndash;&gt;
    <script id="editor1" type="text/plain"></script>
    &lt;!&ndash;富文本编辑器1 end&ndash;&gt;

    <br>

    <el-button type="info" plain @click="addXuanXiang" size="small">增加选项</el-button>
    <br><br>
    &lt;!&ndash; el-row:在该标签中的东西可以在一行显示 &ndash;&gt;
    <el-row v-for="(item,index) in fuxuankuanglist">
      <el-checkbox :label="fuxuankuanglist[index]"></el-checkbox>
      <el-input type="textarea" style="width: 400px" autosize placeholder="请输入内容"
                v-model="textareaValues[index]"></el-input>
      <i class="el-icon-circle-close" @click="deleteXuanXiang(index)"></i>
    </el-row>

    <br><br>

    &lt;!&ndash;富文本编辑器2 start&ndash;&gt;
    <script id="editor2" type="text/plain"></script>
    &lt;!&ndash;富文本编辑器2 end&ndash;&gt;-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "addShiTi",
    data() {
      return {
        //editor1: null,
        //editor2: null,
        loading2: false,
        editor11: null,
        editor22: null,
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        shiTiNanDu: this.domain.shiTiNanDu,        //试题难度
        tikus: [],
        checkList: [],       //选中的答案
        xuanxiang: this.domain.xuanxiang,       //给复选框提供的所有的选项
        fuxuankuanglist: ['A', 'B', 'C', 'D'],  //每个复选框的值
        textareaValues: ['', '', '', ''],        //每个文本框的内容
        formInline: {
          tixingid: '',    //试题类型
          nanduid: null,     //试题难度
          tikuid: '',      //所属题库
          tikuname:'',
          laiyuan: '',     //试题来源
          shitizhuangtai: ''//试题状态
        }
      }
    },
    methods: {
      onSubmit() {   //表单的保存按钮
        this.$data.loading2 = true;

        //alert(JSON.stringify(this.$data.formInline)+"00");  //目前formInline中没有两个富文本编辑框和复选框中的内容，所以需要将这些值赋给formInline
        this.$data.formInline.tigan = this.editor11.getContent();   //将 试卷题干中的数据 赋给formInline对象
        this.$data.formInline.fenxi = this.editor22.getContent();  //将 试卷分析中的数据 赋给formInline对象
        //将 试卷设置中的数据 赋给formInline对象，试卷设置中的数据有3个：1题目的全部选项、2每个题目对应的题目内容、3选中的答案、
        this.$data.formInline.xuanxiang = JSON.stringify(this.$data.fuxuankuanglist);
        this.$data.formInline.xuanxiangmiaoshu = JSON.stringify(this.$data.textareaValues);
        this.$data.formInline.daan = JSON.stringify(this.$data.checkList);
        this.$data.formInline.tikus = JSON.stringify(this.$data.tikus);

        //alert(JSON.stringify(this.$data.formInline)+"11");  //现在formInline中已经有了所有的数据了，开始发送后台请求

        let qs = require("qs");
        this.$axios.post(this.domain.serverpath + "tiku/addShiJuan", qs.stringify(this.$data.formInline)).then((response) => {
          this.$data.loading2 = false;

          if (response.data.success == "ok") {
            this.$data.formInline = {}; //清除form表单中的数据
            this.$data.checkList = [];  //清除复选框的答案
            this.$data.textareaValues = []; //清除试题描述
            this.editor11.setContent("");
            this.editor22.setContent("");

            this.$message({
              message: '恭喜你，添加成功',
              duration: 1500,
              type: 'success'
            });

          }
        }).catch((error) => {
          this.$data.loading2 = false;
          this.$message({
            message: '添加失败',
            duration: 1500,
            type: 'error'
          });
        })

      },
      getTiKuInfoForRedis() {  //从Redis中获取题库数据
        this.$axios.post(this.domain.serverpath + "tiku/getTiKuInfoForRedis").then((response) => {
          //请求成功，给表单中的下拉菜单赋值
          this.$data.tikus = response.data.result;
        }).catch((error) => {

        })
      },
      addXuanXiang() {    //增加复选框选项
        if (this.$data.fuxuankuanglist.length <= 25) {
          let xx = this.$data.xuanxiang[0]//获取选项标识
          let index1 = 0;
          while (true) {
            if (this.$data.fuxuankuanglist.indexOf(xx) == -1) { //说明不存在
              xx = this.$data.xuanxiang[index1];
              break;
            } else {
              index1++;
              xx = this.$data.xuanxiang[index1];
            }
          }
          this.$data.fuxuankuanglist.push(xx);  //添加复选框
          this.$data.textareaValues.push("");   //添加复选框对应的文本框
        }
      },
      deleteXuanXiang(index) {  //删除复选框选项
        let fxk = this.$data.fuxuankuanglist.splice(index, 1);   //删除一个复选框
        this.$data.textareaValues.splice(index, 1);   //删除一个文本框

        //取消选中一个start
        if (this.$data.checkList.length > 0) {  //选中的答案的长度>0，意思有已经选中的复选框
          let check = [];
          for (let i = 0; i < this.$data.checkList.length; i++) {
            if (this.$data.checkList[i] != fxk) {
              check.push(this.$data.checkList);
            }
          }
          this.$data.checkList = check;
        }
      },
      getDatas() {
        //getContent：获取内容
        alert(this.editor1.getContent());
      },
      initEditorInfo() {   //初始化富文本编辑器
        //初始化富文本编辑器start
        //初始化编辑工具的宽高
        //let config1 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        //let config2 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        let config11 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        let config22 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        //初始化editor1  addListener:作用是监听富文本编辑器中的内容
        /*this.editor1 = window.UE.getEditor('editor1', config1);
        this.editor1.addListener('ready', () => {
          this.editor1.setContent("<p>ds&nbsp;<img src=\"http://img.baidu.com/hi/jx2/j_0024.gif\"/><img src=\"http://img.baidu.com/hi/jx2/j_0035.gif\"/></p>")    //给容器中设置初始化内容
        })*/
        //初始化editor2
        /*this.editor2 = window.UE.getEditor('editor2', config2);
        this.editor2.addListener('ready', () => {
          this.editor2.setContent("");
        })*/
        //初始化editor11
        this.editor11 = window.UE.getEditor('editor11', config11);
        this.editor11.addListener('ready', () => {
          this.editor11.setContent("");
        })
        //editor22
        this.editor22 = window.UE.getEditor('editor22', config22);
        this.editor22.addListener('ready', () => {
          this.editor22.setContent("");
        })
        //初始化富文本编辑器end
      }
    },
    mounted() {
      //初始化富文本编辑器
      this.initEditorInfo();
      //初始化所属题库信息
      this.getTiKuInfoForRedis();
    },
    destroyed() {    //也是一个钩子函数，作用：在容器摧毁的同时摧毁富文本编辑器  //destroyed执行的时间：进行页面跳转的时候，在创建下一个页面之前执行
      //this.editor1.destroy();
      //this.editor2.destroy();
      this.editor11.destroy();
      this.editor22.destroy();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
