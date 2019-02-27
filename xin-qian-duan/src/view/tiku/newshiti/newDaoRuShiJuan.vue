<template>
  <div :style="{height:wheight+'px'}">
    <!--批量导入试卷页面start-->

    <div class="leftdiv" style="float:left;width: 50%;">
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
          <el-input style="width: 38%" v-model="formInline.laiyuan" placeholder="原创"></el-input>
        </el-form-item>

        <el-form-item label="试题状态：" prop="shitizhuangtai">
          <el-select v-model="formInline.shitizhuangtai">
            <el-option label="开放" value="1"></el-option>
            <el-option label="关闭" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="rightdiv" style="float:left;width: 50%;">
      <div class="righttopdiv" style="height: 245px">
        <div style="float: left;width: 18.5%">
          <el-upload
            ref="elupload1"
            class="upload-demo"
            action="http://localhost:8888/tiku2/toImportExcelData"
            :name="namefile1"
            :auto-upload="autoupload1"
            :limit="1"
            :show-file-list="showlist"
            :data="mydata1"
            :file-list="fileList">
            <el-button size="small" icon="el-icon-upload2" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传excel文件</div>
          </el-upload>
        </div>
        <div style="float: left;width:50%">
          <el-button type="primary" plain icon="el-icon-download" size="small" @click="mobandownload">下载Excel模板</el-button>
        </div>


      </div>
      <div class="rightfootdiv">
        <el-button type="primary" size="small" @click="onSubmit">提交数据</el-button>
        <el-button type="primary" size="small" plain>重置</el-button>
      </div>
    </div>


    <!--批量导入试卷页面end-->
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "newDaoRuShiJuan",
    data() {
      return {
        wheight: window.innerHeight - 132,
        autoupload1:false,  //是否在选取文件后立即进行上传
        showlist:true,      //是否显示已上传文件列表
        namefile1:"file",   //上传的文件字段名
        mydata1:{},
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        shiTiNanDu: this.domain.shiTiNanDu,        //试题难度
        tikus: [],        //题库
        fileList: [],      //文件上传数据对象
        formInline: {},   //表单数据对象
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
      mobandownload(){
        window.location.href = this.domain.serverpath+"tiku2/downloadExcel";
      },
      onSubmit() {
        //表单验证
        this.$refs['formInline'].validate((valid) => {
          if (valid) {

            let canshu = this.$data.formInline;
            this.$data.mydata1.canshu=JSON.stringify(canshu);
            //手动提交文件列表
            this.$refs.elupload1.submit();

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
      }

    },
    mounted() {
      this.getTikusToRedis()
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
