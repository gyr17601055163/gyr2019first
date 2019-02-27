<template>
  <div>


    <el-form :model="updateformInline" class="demo-form-inline">
      <el-form-item label="试题类型：">
        <el-select v-model="updateformInline.tixingid" placeholder="多选题">
          <el-option v-for="(item,index) in shiTiLeiXing" :key="index" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题难度：">
        <el-rate style="margin-top: 12px" v-model="updateformInline.nanduid" show-text
                 score-template="{nanduid}"></el-rate>
      </el-form-item>

      <el-form-item label="所属题库：">
        <el-select v-model="updateformInline.tikuid" placeholder="所属题库">
          <el-option v-for="(items,index) in tikus" :label="items.tikuname" :key="index"
                     :value="items.id"></el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="试题来源：">
        <el-input style="width: 25.5%" v-model="updateformInline.laiyuan" placeholder="原创"></el-input>
      </el-form-item>

      <el-form-item label="试题状态：">
        <el-select v-model="updateformInline.shitizhuangtai" placeholder="开放">
          <el-option label="开放" value="1"></el-option>
          <el-option label="关闭" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="试题题干：">
        <div style="float: left;width: 200px"></div>
        <div style="float: left">
          <!--富文本编辑器1 start-->
          <script id="editor111" type="text/plain"></script>
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
        <div style="float: left;width:500px;height:500px">
          <!--富文本编辑器1 start-->
          <script id="editor222" type="text/plain"></script>
          <!--富文本编辑器1 end-->
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "updateShiTi",
    data() {
      return {
        editor111: null,
        editor222: null,
        updateformInline: {
          tixingid: '',    //试题类型
          nanduid: null,     //试题难度
          tikuid: '',      //所属题库
          laiyuan: '',     //试题来源
          shitizhuangtai: ''//试题状态
        },
      }
    },
    methods: {
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
        let config111 = {initialFrameWidth: 400, initialFrameHeight: 140}
        let config222 = {initialFrameWidth: 400, initialFrameHeight: 140}
        //初始化editor11
        this.editor111 = window.UE.getEditor('editor111', config111);
        this.editor111.addListener('ready', () => {
          this.editor111.setContent("");
        })
        //editor22
        this.editor222 = window.UE.getEditor('editor222', config222);
        this.editor222.addListener('ready', () => {
          this.editor222.setContent("");
        })
        //初始化富文本编辑器end
      }
    },
    mounted(){
      //初始化富文本编辑器
      this.initEditorInfo();
    },
    destroyed() {    //也是一个钩子函数，作用：在容器摧毁的同时摧毁富文本编辑器  //destroyed执行的时间：进行页面跳转的时候，在创建下一个页面之前执行
      this.editor111.destroy();
      this.editor222.destroy();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
