<template>
    <div >
      <el-form :inline="true" :model="formInline" :style="{'margin-top':10+'px'}" class="demo-form-inline">
        <el-form-item label="角色名称">
          <el-input v-model="formInline.roleName" placeholder="角色名称"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <el-button size="small" icon="el-icon-plus" type="primary" @click="addRole">添加角色</el-button>
      <el-table
        v-loading="loading"
        height="450"
        element-loading-text="拼命处理中>_<"
        :data="tableData"
        border
        :highlight-current-row="hightlight"
        style="width: 100%">

        <el-table-column
          v-if="show"
          prop="id"
          label="角色ID"
          width="120">
        </el-table-column>
        <el-table-column
          v-if="show"
          prop="userName"
          label="用户名"
          width="120">
        </el-table-column>

        <el-table-column
          type="index"
          :index="indexmethod"
          prop="suoyin"
          label="序号"
          width="120">
        </el-table-column>

        <el-table-column
          prop="name"
          align="center"
          label="角色名称"
          width="">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>角色名称: {{ scope.row.name }}</p>
              <p>角色描述: {{ scope.row.miaoshu }}</p>
              <p>捆绑用户: {{ scope.row.userName }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.name }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          prop="miaoshu"
          label="角色描述"
          width="">
        </el-table-column>

        <el-table-column
          fixed="right"
          label="操作"
          width="">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="deleteInfo(scope.row)">删除</el-button>
            <el-button type="primary" size="mini" @click="bianji(scope.row)">编辑绑定权限</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页start -->
      <mypage ref="pageRef"  @toforpage="toPage"></mypage>
      <!-- 分页end -->

      <!-- 添加角色的弹出层 -->
      <el-dialog
        title="添加角色"
        :visible.sync="dialogVisible"
        width="30%"
        >
        <roleAdd @closeAddRole="closeAddRole"></roleAdd><!-- 角色表单 -->
      </el-dialog>

      <!-- 编辑角色的弹出层 可以进行角色权限的分配-->
      <el-dialog
        title="编辑角色"
        :visible.sync="dialogVisible2"
        width="60%"
        height="60%"
      >

         <roleEdit @roleEidit="closeAddRole" ref="roleEdit"></roleEdit>

      </el-dialog>


    </div>
</template>

<script>
    import mypage from '../../common/pagefoot.vue'
    import roleAdd from './roleAdd.vue'
    import roleEdit from './roleEdit.vue'

    export default {
        components:{
          mypage,
          roleAdd,
          roleEdit
        },
        name: "roleManger",
        data(){
          return{
            dialogVisible:false,
            dialogVisible2:false,
            show:false,
            hightlight:true,
            hidden:true,
            loading:false,
            tableData:[],
            formInline:{
              roleName:''
            }
          }
        },
        methods:{
          addRole(){
            //添加新角色,打开弹出层
            this.$data.dialogVisible=true
          },
          closeAddRole(){//关闭添加的弹出层
            this.$data.dialogVisible=false
            this.$data.dialogVisible2=false
            //刷新数据
            this.toPage({})
          },
          toPage(pageInfo){//这是分页方法，每个分页的页面都要用该方法
            //有新的查询参数可以这么设置start
            pageInfo.roleName=this.$data.formInline.roleName

            //有新的查询参数可以这么设置end
            this.$axios.post(this.domain.serverpath+"role/getRolelistPage",JSON.stringify(pageInfo))
              .then((response)=>{

                //分页的下标索引start
                this.domain.userinfo.pageNo=response.data.result.pageNo
                this.domain.userinfo.pageSize=response.data.result.pageSize
                //分页的下标索引end

                this.$data.tableData=response.data.result.resultList
                //给分页组件pagefoot设置总条数
                this.$refs.pageRef.total=response.data.result.totalCount
              })
          },
          onSubmit(){//查询表单的提交
            let pageInfo={};
            this.toPage(pageInfo)
          },
          indexmethod(index) {//计算索引的方法
            return index+(this.domain.userinfo.pageNo-1)*this.domain.userinfo.pageSize+1;
          },
          deleteInfo(row){//删除一个角色数据

            if(row.userName!=null && row.userName!=''){
              this.$message({
                showClose: true,
                duration:2000,
                message: '该角色有绑定的用户，请先解绑后再进行删除！',
                type: 'warning'
              });
            }else{
              //打开加载等待窗口
              this.$data.loading=true
              this.$axios.post(this.domain.serverpath+'role/deleteRole',JSON.stringify(row)).then((response)=>{
                if(response.data.success=="ok"){
                  this.$notify({
                    title: '成功',
                    message: '删除成功！',
                    type: 'success'
                  });
                }
                //刷新列表
                this.toPage({})
                //关闭加载等待窗口
                this.$data.loading=false
              }).catch((error)=>{
                this.$data.loading=false
                this.$router.push({path:"/error"})
              })
            }
          },
          bianji(row){//编辑一行数据
            //打开弹出层
            this.$data.dialogVisible2=true
            //打开的时候回显一下数据
            this.$axios.post(this.domain.serverpath+'menu/getTreeMenu2',JSON.stringify({roleId:row.id})).then((response)=>{
                let role={}
                role.name=row.name;
                role.miaoshu=row.miaoshu;
                role.id=row.id;
                //参数1树状的数据 参数2 选中的数据  参数3回显的角色数据
                this.$refs.roleEdit.setTreeData(response.data.result,response.data.menuIds,role);

            }).catch((error)=>{

            })
          }
        },
        mounted(){
            //获取角色列表数据
            //打开数据加载动态图
            this.$data.loading=true;
            this.$axios.post(this.domain.serverpath+'role/getRolelistPage',{}).then((response)=>{
            //关闭加载数据动态图
            this.$data.loading=false;
            //响应结果
            this.$data.tableData=response.data.result.resultList
            //给分页组件设置总的记录数
            this.$refs.pageRef.total=response.data.result.totalCount

            //分页的下标索引start
            this.domain.userinfo.pageNo=response.data.result.pageNo
            this.domain.userinfo.pageSize=response.data.result.pageSize
            //分页的下标索引end

          })
        }
    }
</script>

<style scoped>

</style>
