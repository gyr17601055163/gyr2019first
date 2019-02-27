<template>
  <div>

    <!--auto-upload	是否在选取文件后立即进行上传
        data	上传时附带的额外参数
        name	上传的文件字段名,这里使用它的默认值file
        action	必选参数，上传的地址-->


    <el-upload
      ref="elupload"
      action="http://localhost:8888/user/girlUserEdit"
      :auto-upload="autoupload"
      limit="1"
      name="file"
      :data="mydata"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove">
      <i class="el-icon-plus"></i>
    </el-upload>


    <br>

    <!--表单start-->
    <el-form :model="ruleForm2" :inline="true" ref="ruleForm2" class="demo-ruleForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="ruleForm2.username"></el-input>
      </el-form-item>
      <el-form-item label="登录名" prop="loginname">
        <el-input v-model="ruleForm2.loginname"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm2')">提交</el-button>
        <el-button @click="resetForm('ruleForm2')">重置</el-button>
      </el-form-item>
    </el-form>
    <!--表单end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "girlUserUpdate",
    data() {
      return {
        autoupload:false,
        mydata:{},
        imageUrl: '',
        ruleForm2: {
          username: "",
          loginname: ""
        }

      }
    },
    methods: {
      submitForm(formid) {

        let canshu=JSON.stringify(this.$data.ruleForm2)
        this.$data.mydata.canshu=canshu
        //设置上传图片时额外携带的参数,将表格中的数据对象赋值给girlData
        //点击提交按钮的时候，手动提交图片,submit	手动上传文件列表，this.$refs.girlUpload获取提交图片的组件
        this.$refs.elupload.submit()

      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
