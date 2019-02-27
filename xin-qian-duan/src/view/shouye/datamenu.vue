<template>
    <el-menu ref="emenu" default-active="2"
             class="el-menu-vertical-demo"
             :collapse="isCollapse"
             style="background-color:#EEF1F6;"
             >

        <el-submenu :title="getMenuName(m1.menuName)" v-for="(m1,mindex) in listMenu" :key="mindex" :index="getIndex(m1.id)">
          <template slot="title">
            <i v-if="m1.id==1" class="el-icon-setting"></i>
            <i v-if="m1.id==5" class="el-icon-goods"></i>
            <i v-if="m1.id==12" class="el-icon-sold-out"></i>
            <i v-if="m1.id==1902141738020000" class="el-icon-upload"></i>
            <i v-if="m1.id==1902180934390000" class="el-icon-edit-outline"></i>
            <i v-if="m1.id==1902202333530000" class="el-icon-loading"></i>
            <span slot="title">{{m1.menuName}}</span>
          </template>

          <div v-for="(m2,m2Index) in m1.listMenu" :key="m2Index">
            <el-submenu v-if="m2.listMenu!=null&&m2.listMenu.length > 0" :index="getIndex(m2.parentMenuId)+'-'+getIndex(m2.id)" >
              <span slot="title" >{{m2.menuName}}</span>
              <el-menu-item @click="toforPath(m3.url)" v-for="(m3,m3Index) in m2.listMenu" :key="m3Index" :index="getIndex(m2.parentMenuId)+'-'+getIndex(m3.parentMenuId)+'-'+getIndex(m3.id)">{{m3.menuName}}</el-menu-item>
            </el-submenu>
            <el-menu-item v-else @click="toforPath(m2.url)" :index="getIndex(m2.parentMenuId)+'-'+getIndex(m2.id)">{{m2.menuName}}</el-menu-item>
          </div>

        </el-submenu>
    </el-menu>
</template>

<script>
    export default {
      name: "datamenu",
      data(){
          return{
            isCollapse:true,
            listMenu:[],
            wheight:window.innerHeight
          }
      },
      methods:{
        openClose(){
          if(this.$refs.emenu.collapse){
            this.$data.isCollapse=false;
            //调用父组件的方法，更改变量值
            this.$emit("ee",false)

          }else{
            this.$data.isCollapse=true;
            //调用父组件的方法，更改变量值
            this.$emit("ee",true)

          }
        },
        getIndex(index){
          return ''+index.toString();
        },
        getMenuName(menuName){
          return menuName;
        },
        toforPath(url){
           if(url!=null){
             //进入对应的路由
             this.$router.push({path:url})
           }else{
             this.$router.push({path:'/datamain/test'})
           }

        }

      },
      mounted(){
          this.$axios.post(this.domain.serverpath+"menu/treeMenu").then((reponse)=>{
              //为data中的listMenu赋值
              this.listMenu=reponse.data.result;
          }).catch((error)=>{
            console.log("===error==="+error.message);
          })
      }
    }
</script>

<style>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }

</style>
