<template>
  <div
    v-loading="loading"
    element-loading-text="处理中！"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  >
    <el-form
             :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="miaoshu">
        <el-input type="textarea" v-model="ruleForm.miaoshu"></el-input>
      </el-form-item>

        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="info" @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
    export default {
        name: "roleAdd",
        data(){
          var checkname=(rule,value,callback)=>{
            if (!value) {
              return callback(new Error('角色名称不能为空！'));
            }else{
              return callback()
            }
          };
          var checkmiaoshu=(rule,value,callback)=>{
            if(!value){
              return callback(new Error('角色描述不能为空！'));
            }else{
              return callback()
            }
          };
          return{
            loading:false,
            rules:{
              name:[
                {validator:checkname,trigger:'blur'}
              ],
              miaoshu:[
                {validator:checkmiaoshu,trigger:'blur'}
              ]
            },
            ruleForm:{
              name:'',
              miaoshu:''
            }
          }
        },
      methods:{
        submitForm(formName){//提交表单的验证
           //打开加载窗
           this.$data.loading=true
           this.$refs[formName].validate((valid)=>{
              if(valid){
                 let formdata=this.$data.ruleForm
                formdata.roleId=
                 this.$axios.post(this.domain.serverpath+"role/toAddRole",JSON.stringify(formdata)).then((response)=>{
                   //关闭加载
                   this.$data.loading=false
                   //清空数据
                   this.$data.ruleForm={}
                   //调用父页面的关闭方法，关闭添加弹出层
                   this.$emit('closeAddRole')
                   //刷新列表数据

                   //弹窗提示
                   this.$notify({
                     title: '成功',
                     message: '添加成功！',
                     type: 'success'
                   });

                 }).catch((error)=>{
                   //关闭加载
                   this.$data.loading=false
                   //弹窗提示
                   this.$notify({
                     title: '出错',
                     message: '添加出错联系管理员！',
                     type: 'error'
                   });
                 })
              }else{
                //关闭屏蔽层
                this.$data.loading=false
              }
           })
        },
        resetForm(){//重置表单
          this.$data.ruleForm={}
        }
      }
    }
</script>

<style scoped>

</style>
