<template>
    <div>
      <el-upload
        ref="upload"
        name="file"
        class="upload-demo"
        action="http://localhost:8888/user/addUserInfo"
        :on-success="onFileSuccess"
        :on-remove="handleRemove"
        :limit="fileNumber"
        :data="userdata"
        :file-list="fileList2"
        :auto-upload="autoupload"
        list-type="picture">
        <el-button size="small" type="primary">点击选择图像</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <br>
      <!-- 用户的form表单 -->
      <el-form :inline="true"  :model="formInline" size="mini" class="demo-form-inline">
        <el-form-item label="用户名">
          <el-input v-model="formInline.userName" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="登录名">
          <el-input v-model="formInline.loginName" placeholder="登录名"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="formInline.sex">
            <el-radio-button label="男生" name="sex" class="el-icon-edit"></el-radio-button>
            <el-radio-button label="女生" name="sex"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <br>
        <el-form-item label="密码">
          <el-input v-model="formInline.password" type="password" placeholder="密码"></el-input>
        </el-form-item>
        <br>
        <el-form-item label="确认密码">
          <el-input v-model="formInline.password2" type="password" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>

      <el-button type="success" size="small" @click="submitUpload">保存</el-button>
      <el-button type="danger" size="small" @click="formReset">重置</el-button>
    </div>
</template>

<script>
    export default {
        name: "userAdd",
        data(){
          return{
            autoupload:false,
            fileNumber:1,
            fileList2:[],
            formInline:{//用户的文字数据
              userName:'',
              password:'',
              password2:'',
              sex:'',
              buMen:''
            },
            userdata:{

            }
          }
        },
        methods:{
          submitUpload() {
            let canshu=JSON.stringify(this.$data.formInline);
            this.$data.userdata.canshu=canshu
            this.$refs.upload.submit();
          },
          handleRemove(file, fileList) {
            console.log(file, fileList);
          },
          onFileSuccess(response, file, fileList) {
             //重置表单，关闭天窗
            //清除图片
            this.$data.fileList2=[]
            //清除form表单的内容
            this.$data.formInline={}
            //关闭弹出层

          },
          formReset(){
            //清除图片
            this.$data.fileList2=[]
            //清除form表单的内容
            this.$data.formInline={}
          }
        }
    }
</script>

<style scoped>

</style>
