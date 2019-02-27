<template>
  <div style="width:100%;height: 100%;">
    <el-container style="height: 100%" direction="vertical">
      <el-header style="font-size: 12px;text-align: left">
        <span style="font-size:25px;margin-right:80%;">
            蛟龙在线
            <el-tooltip ref="tip" class="item" effect="dark"  placement="top-start" v-bind:content="mycontent">
              <i class="el-icon-menu" @click="openParentClose()"></i>
          </el-tooltip>
        </span>

        <el-dropdown @command="handleCommand">
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="a">查看</el-dropdown-item>
            <el-dropdown-item command="b">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span ref="userinfo_username">{{this.$data.username}}</span>
        <input ref="userinfo_userid" type="hidden" v-model="userid">
      </el-header>

      <el-container noresize:false>
        <!-- 左侧的菜单 -->
        <mymenu  ref="iiii" v-on:ee="updatePro"></mymenu>
        <!-- 右侧的主显示区域 -->
        <mymain style="padding:0px"></mymain>

      </el-container>
    </el-container>

    <el-dialog
          title="当前用户信息"
          :visible.sync="dialog1Visible"
          width="40%"
          >
            <img src="http://127.0.0.1:8090/USER/2019-01-16/" width="150px">
            <el-form  :inline="true" label-width="100px" class="demo-form-inline">
              <el-form-item label="用户名:">{{this.$data.currInfo.userName}}</el-form-item>
              <el-form-item label="登录名:">{{this.$data.currInfo.loginName}}</el-form-item>
              <el-form-item label="性别:">{{this.$data.currInfo.sex}}</el-form-item>
              <br>
              <el-form-item label="电话:">{{this.$data.currInfo.tel}}</el-form-item>
              <el-form-item label="部门:">{{this.$data.currInfo.buMen}}</el-form-item>
            </el-form>

          <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialog1Visible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import mymenu from './datamenu.vue'
  import mymain from './datamain.vue'
  const userinfo={};
  export default {
    data(){
      return{
        mycontent:"点击打开菜单",
        dialogVisible: false,
        dialog1Visible: false,
        userid:this.domain.userinfo.userid,
        username:this.domain.userinfo.username,
        currInfo:{
          userName:'',
          loginName:'',
          sex:'',
          tel:'',
          buMen:''
        }
      }
    },
    components:{mymenu,mymain},
    name: "shouye",
    methods:{
      tttt(){
        alert("pppp999999");
      },
      openParentClose(){
        //调用子组件menu中的openClose()
        this.$refs.iiii.openClose();
      },
      updatePro(uu){
        if(uu){
          this.$data.content="点击打开菜单";
        }else{
          this.$data.content="点击关闭菜单";
        }
      },
      handleCommand(command){

          if(command=="b"){//退出操作

            this.$confirm('确认登出？').then(_ => {
               this.$axios.post(this.domain.serverpath+"loginout").then((response)=>{
                   let sts=response.data.success;
                   if(sts=="ok"){
                      this.$router.push({path:'/'});
                   }
               })

            }).catch(_ => {

            });

          }else if(command=="a"){
             //获取一下隐藏域中的用户ID
             let userid= this.$refs.userinfo_userid.value;
             //到后台后获取用户的信息
             this.$axios.post(this.domain.serverpath+"user/getUserInfo",JSON.stringify({userid:userid})).then((response)=>{
                 //获取用户信息
                 let userinfo=response.data.result;
                 //打开用户信息的弹出层
                 this.$data.dialog1Visible=true;
                 //填充用户数据
                 this.$data.currInfo=response.data.result
             })

          }
      }
    },
    mounted(){
       if(this.$route.query.username!=null&& this.$route.query.username!='undefind'){

         this.domain.userinfo.username=this.$route.query.username;
         this.domain.userinfo.userid=this.$route.query.userid;

       }
       this.$data.username=this.domain.userinfo.username;
       this.$data.userid=this.domain.userinfo.userid;
    }
  }
</script>

<style scoped>
  .el-header {
    background-color: #409EFF;
    color: #333;
    line-height:60px;

  }

  .el-aside {
    color: #333;
    height: 100%;
  }
</style>
