<template>
  <div
    v-loading="loading"
    style="width: 100%"
  >
    <!--新增试卷页面start-->

    <el-form :model="formInline" :rules="rules" ref="formInline" class="demo-form-inline">
      <el-form-item label="试题类型：" prop="tixingid">
        <el-select v-model="formInline.tixingid">
          <el-option v-for="(item,index) in shiTiLeiXing" :key="index" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题难度：" prop="nanduid">
        <el-rate style="margin-top: 12px" v-model="formInline.nanduid" show-text :texts="shiTiNanDu"></el-rate>
      </el-form-item>

      <el-form-item label="所属题库：" prop="tikuid">
        <el-select v-model="formInline.tikuid" placeholder="所属题库">
          <el-option v-for="(item,index) in tikus" :key="index" :label="item.tikuname" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题来源：" prop="laiyuan">
        <el-input style="width: 25.5%" v-model="formInline.laiyuan" placeholder="原创"></el-input>
      </el-form-item>

      <el-form-item label="试题状态：" prop="shitizhuangtai">
        <el-select v-model="formInline.shitizhuangtai">
          <el-option label="开放" value="1"></el-option>
          <el-option label="关闭" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题题干：">
        <div style="float: left;width: 200px"></div>
        <div style="float: left">
          <!--富文本编辑器1 start-->
          <script id="editor1" type="text/plain"></script>
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
          <script id="editor2" type="text/plain"></script>
          <!--富文本编辑器1 end-->
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit('formInline')">保存</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>

    <!--新增试卷页面end-->
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "newAddShiJuan",
    data() {
      return {
        loading:false,  //加载框
        editor1: null, //富文本编辑器1
        editor2: null, //富文本编辑器2
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        shiTiNanDu: this.domain.shiTiNanDu,        //试题难度
        tikus: [],
        checkList: [],       //选中的答案
        xuanxiang: this.domain.xuanxiang,       //给复选框提供的所有的选项
        fuxuankuanglist: ['A', 'B', 'C', 'D'],  //每个复选框的值
        textareaValues: ['', '', '', ''],        //每个文本框的内容
        formInline: {},   //新增表单的数据对象
        rules: {    //表单验证
          tixingid: [
            {required: true, message: '请选择试题类型', trigger: 'change'},
          ],
          nanduid: [
            {required: true, message: '请选择试题难度', trigger: 'change'}
          ],
          tikuid: [
            {required: true, message: '请选择所属题库', trigger: 'change'}
          ],
          laiyuan: [
            {required: true, message: '请填写试题来源', trigger: 'blur'}
          ],
          shitizhuangtai: [
            {required: true, message: '请选择试题状态', trigger: 'change'}
          ]
        }
      }
    },
    methods: {
      onSubmit(formInline) { //添加
        //表单验证
        this.$refs[formInline].validate((valid) => {
          if (valid) {
            //打开加载框
            this.$data.loading = true;

            //获取form表单中的所有的数据
            let formInfo = this.$data.formInline;
            //题型name
            formInfo.tixingname = this.domain.shiTiLeiXing[this.$data.formInline.tixingid-1].label;
            //试题难度
            formInfo.nanduname = this.domain.shiTiNanDu[this.$data.formInline.nanduid-1];
            //试题题干
            formInfo.tigan = this.editor1.getContent();
            //试题设置
            formInfo.daan = JSON.stringify(this.$data.checkList);   //试题答案
            formInfo.xuanxiang = JSON.stringify(this.$data.fuxuankuanglist);  //选项
            formInfo.xuanxiangmiaoshu = JSON.stringify(this.$data.textareaValues);  //选项描述
            //试题分析
            formInfo.fenxi = this.editor2.getContent();

            let qs = require("qs");

            this.$axios.post(this.domain.serverpath + "tiku2/addShiJuan",qs.stringify(formInfo)).then((response) => {
              if(response.data.success == "ok"){
                //关闭加载框
                this.$data.loading = false;
                //清空表单中的数据
                this.$data.formInline = {};
                this.editor1.setContent("")
                this.editor2.setContent("")
                this.$data.checkList = [];
                this.$data.fuxuankuanglist = ['A', 'B', 'C', 'D'];
                this.$data.textareaValues = ['', '', '', ''];

                this.$message({
                  message: '恭喜你，添加成功',
                  duration: 1500,
                  type: 'success'
                });
              }
            }).catch((error) => {
              //关闭加载框
              this.$data.loading = false;

              this.$message({
                message: '添加试题失败',
                duration: 1500,
                type: 'error'
              });
            })

          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      getTikusToRedis() {  //从Redis中获取题库信息
        this.$axios.post(this.domain.serverpath + "tiku2/getTiKusToRedis").then((response) => {
          this.$data.tikus = response.data.result
          //alert(JSON.stringify(this.$data.tikus))
        }).catch((error) => {
          this.$notify({
            title: '警告',
            message: '没有获取到可用的题库信息！',
            type: 'warning'
          });
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
      initEditorInfo() {   //初始化富文本编辑器
        //初始化富文本编辑器start
        //初始化编辑工具的宽高
        let config1 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        let config2 = {initialFrameWidth: 1000, initialFrameHeight: 140}
        //初始化editor1  addListener:作用是监听富文本编辑器中的内容
        this.editor1 = window.UE.getEditor('editor1', config1);
        this.editor1.addListener('ready', () => {
          this.editor1.setContent("");
        })
        //editor2
        this.editor2 = window.UE.getEditor('editor2', config2);
        this.editor2.addListener('ready', () => {
          this.editor2.setContent("");
        })
        //初始化富文本编辑器end
      }
    },
    mounted() {
      //初始化富文本编辑器
      this.initEditorInfo();
      this.getTikusToRedis();
    },
    destroyed() {    //也是一个钩子函数，作用：在容器摧毁的同时摧毁富文本编辑器  //destroyed执行的时间：进行页面跳转的时候，在创建下一个页面之前执行
      this.editor1.destroy();
      this.editor2.destroy();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
