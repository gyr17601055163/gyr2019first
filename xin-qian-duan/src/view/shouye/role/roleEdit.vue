<template>
    <div
      v-loading="loading"
      element-loading-text="处理中！"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
        >

        <!-- 显示角色信息start -->
        <el-form
          :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="角色名称" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
            <input type="hidden" name="id" v-model="ruleForm.id">
          </el-form-item>
          <el-form-item label="角色描述" prop="miaoshu">
            <el-input type="textarea" v-model="ruleForm.miaoshu"></el-input>
          </el-form-item>

        </el-form>
        <!-- 显示角色信息end -->
      <div class="custom-tree-container">
          <div class="block">
            <p>权限列表信息</p>
            <el-tree
              ref="treeref"
              :data="dataTree"
              show-checkbox
              check-strictly:true
              node-key="id"
              :default-expand-all="defaultexpandall"
              :default-checked-keys="checkedkeys"
              :props="defaultProps">
            </el-tree>
          </div>
      </div>


        <!-- 显示权限信息 加载权限信息-->
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button type="info" @click="resetForm()">重置</el-button>
    </div>
</template>

<script>
    export default {
        name: "roleEdit",
        data(){
          var checkname=(rule,value,callback)=>{
            if (!value) {
              return callback(new Error('角色名称不能为空！'));
            }else{
              return callback()
            }
          };
          var checkmiaoshu=(rule,value,callback)=>{
            if (!value) {
              return callback(new Error('角色描述不能为空！'));
            }else{
              return callback()
            }
          };
          return{
            loading:false,
            defaultProps: {
              id:'id',
              children: 'listMenu',
              label: 'label',
              leval:'leval',
              parentMenuId:'parentMenuId',
            },
            dataTree:[],
            defaultexpandall:true,
            checkedkeys:[],//勾选中的项目，这一级别的只去取等级是第三和第四的
            ruleForm:{
              name:'',
              id:'',
              miaoshu:''
            },
            rules:{
               name:[
                 {validator:checkname,trigger:'blur'}
               ],
               miaoshu:[
                 {validator:checkmiaoshu,trigger:'blur'}
               ]
            }
          }
        },
        methods:{
          submitForm(formName){//提交ruleForm表单
            this.$refs[formName].validate((valid)=>{

               if(valid){
                 //ruleForm
                 let role=this.$data.ruleForm;
                 let menuIds="";
                 let menuarray=this.$refs.treeref.getCheckedNodes(false,true);
                 for(let i=0;i<menuarray.length;i++){
                     if(i==(menuarray.length-1)){
                       menuIds=menuIds+menuarray[i].id
                     }else{
                       menuIds=menuIds+menuarray[i].id+":"
                     }
                 }
                 role.menuIds=menuIds;
                 this.$data.loading=true;
                 this.$axios.post(this.domain.serverpath+'role/eidtRoleMenu',JSON.stringify(role)).then((response)=>{

                   if(response.data.success=="ok"){
                     this.$data.loading=false;
                     //弹出提示窗
                     this.$notify({
                       title: '成功',
                       message: '操作处理成功！',
                       type: 'success'
                     });
                     //清除表单
                     this.$data.ruleForm={}
                     //关闭弹窗
                     this.$emit("roleEidit")
                   }

                 }).catch((error)=>{
                    this.$data.loading=false;
                 })

               }else{

               }

            })
          },
          setTreeData(treeData,checkNodeKeys,role){//设置树状的数据节点
             this.dataTree=treeData;
             this.checkedkeys=checkNodeKeys;
             this.ruleForm=role;
          }
        },
        mounted(){

        }
    }
</script>

<style scoped>

</style>
