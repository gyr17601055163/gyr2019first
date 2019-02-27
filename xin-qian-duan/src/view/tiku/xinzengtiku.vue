<template>
  <div :style="{height:wheight+'px'}"

       v-loading="loading2"
       element-loading-text="玩命处理中"
       element-loading-spinner="el-icon-loading"
       element-loading-background="rgba(0, 0, 0, 0.8)"
  >

    <!--新增题库表单start-->
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" :style="{width:'30%'}">
      <el-form-item label="题库名称:" prop="tikuname">
        <el-input v-model="form.tikuname"></el-input>
      </el-form-item>
      <el-form-item label="状态:" prop="tikuzhuangtai">
        <el-select v-model="form.tikuzhuangtai" placeholder="关闭状态下不允许存取试题">
          <el-option label="开放" value="1"></el-option>
          <el-option label="关闭" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="题库说明:" prop="tikushuoming">
        <el-input type="textarea" v-model="form.tikushuoming"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')">提交页面</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
    <!--新增题库表单end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "xinzengtiku",
    data() {
      return {
        wheight: window.innerHeight - 132,
        loading2: false,
        form: {
          tikuname: '',
          tikuzhuangtai: '',
          tikushuoming: ''
        },
        rules: {
          tikuname: [
            {required: true, message: '请填写题库名称', trigger: 'blur'},
          ],
          tikuzhuangtai: [
            {required: true, message: '请选择题库状态', trigger: 'change'}
          ],
          tikushuoming: [
            {required: true, message: '请填写题库说明', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      onSubmit(form) { //提交页面按钮

        this.$refs[form].validate((valid) => {
          if (valid) {
            this.$data.loading2 = true;

            let formInfo = {};
            formInfo = this.$data.form;
            let qs = require("qs");

            this.$axios.post(this.domain.serverpath + "tiku/xinZengTiKu", qs.stringify(formInfo)).then((response) => {
              if (response.data.success == "ok") {
                this.$data.loading2 = false;
                this.$data.form = {};
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
          } else {
            console.log('error submit!!');
            return false;
          }
        });

      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
