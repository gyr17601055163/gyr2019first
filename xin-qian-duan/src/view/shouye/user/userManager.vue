<template>
  <div>

    <el-form :inline="true" :model="formInline" :style="{'margin-top':10+'px'}" class="demo-form-inline">
      <el-form-item label="用户名">
        <el-input v-model="formInline.userName" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-col :span="11">
          <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"
                          v-model="formInline.startDate" style="width: 100%;"></el-date-picker>
        </el-col>
        <el-col class="line" :span="2">-</el-col>
        <el-col :span="11">
          <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" v-model="formInline.endDate"
                          style="width: 100%;"></el-date-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="formInline.sex" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <el-button size="small" icon="el-icon-plus" type="primary" @click="addUser">添加新用户</el-button>
    <el-table
      v-loading="loading"
      height="450"
      element-loading-text="拼命加载中>_<"
      :data="tableData"
      style="width: 100%">
      <el-table-column
        type="selection"
        prop="id"
        width="55">
      </el-table-column>
      <el-table-column
        prop="userName"
        label="用户名">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>
              <img src="../../../assets/logo.png" height="40px">
            </p>
            <p>用户名: {{ scope.row.userName }}</p>
            <p>登录名: {{ scope.row.loginName }}</p>
            <p>性 别: {{ scope.row.loginName }}</p>
            <p>电 话: {{ scope.row.tel}}</p>
            <p>部 门: {{ scope.row.buMen }}</p>
            <p>角 色: {{ scope.row.roleName }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.userName }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        prop="loginName"
        sortable
        label="登录名">
      </el-table-column>
      <el-table-column
        prop="sex"
        label="性别">
      </el-table-column>
      <el-table-column
        prop="tel"
        label="电话">
      </el-table-column>
      <el-table-column
        prop="buMen"
        label="部门">
      </el-table-column>
      <el-table-column
        prop="roleName"
        label="已绑定角色">
      </el-table-column>
      <el-table-column
        width="350"
        prop="address"
        label="操作">

        <template slot-scope="scope">   <!--slot-scope="scope" 获取到该行表格的所有数据-->
          <el-button
            size="mini"
            type="danger"
            @click="deleteRole(scope.row)">删除
          </el-button>

          <el-button size="mini"
                     type="primary"
                     @click="useredit(scope.row)"
                     plain>编 辑
          </el-button>

          <el-button size="mini"
                     type="primary"
                     @click="girlUseredit(scope.row)"
                     plain>girl - 编 辑
          </el-button>

          <el-button
            size="mini"
            type="primary"
            @click="bandRoleForUser(scope.row)">绑定角色
          </el-button>
        </template>

      </el-table-column>
    </el-table>

    </el-table-column>
    </el-table>

    <mypage ref="pageRef" tableData="tableData" @toforpage="toPage"></mypage>

    <!-- 添加弹出层 start-->
    <el-dialog
      title="添加新用户"
      :visible.sync="dialogVisible"
      width="45%"
    >
      <!-- 用户信息组件 -->
      <useradd></useradd>

      <!--<el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>-->
      </span>
    </el-dialog>
    <!-- 添加弹出层 end-->

    <!-- 弹出绑定的角色start -->
    <el-dialog title="绑定角色" :visible.sync="dialogTableVisible3">
      <template>
        角色：
        <el-select
          v-model="roleValue"
          multiple
          filterable
          remote
          reserve-keyword
          placeholder="请输入关键词"
          :remote-method="remoteMethod"
          :loading="loading8">
          <el-option
            v-for="item in roleOptions"
            :key="item.lable"
            :label="item.value"
            :value="item.lable">
          </el-option>
        </el-select>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userQuxiao">取 消</el-button>
        <el-button type="primary" @click="userBaoCun">保 存</el-button>
      </div>
    </el-dialog>
    <!-- 弹出绑定的角色end -->

    <!-- 编辑新用户 editstart -->
    <el-dialog
      title="用户信息更新"
      :visible.sync="dialogTableVisibleUpdate"
      width="45%"
    >
      <userupdate ref="updateUserInfo"></userupdate>
    </el-dialog>
    <!-- 编辑新用户 editend -->


    <!--girl编辑start-->
    <el-dialog
      title="GIRL - 编辑"
      :visible.sync="girlDialogVisible"
      width="45%">

      <girluserupdate></girluserupdate>

    </el-dialog>
    <!--girl编辑end-->

  </div>
</template>

<script>
  //引入分页组件
  import mypage from '../../common/pagefoot.vue'
  import useradd from './userAdd.vue'
  import userupdate from '@/view/shouye/user/userUpdate.vue'
  import girluserupdate from './girlUserUpdate'

  export default {
    name: "usermanager",
    components: {
      mypage,
      useradd,
      userupdate,
      girluserupdate
    },
    data() {
      return {
        rowData: {},
        roleValue: [],
        userId: "",
        loading8: false,
        dialogVisible: false,
        dialogTableVisibleUpdate: false,
        dialogTableVisible3: false,
        tableData: [],
        loading: false,
        formInline: {
          userName: '',
          startDate: '',
          endDate: '',
          sex: ''
        },
        options: [
          {
            value: '',
            label: '全部'
          },
          {
            value: '1',
            label: '男'
          }, {
            value: '0',
            label: '女'
          }
        ],
        //角色列表
        roleOptions: [],
        girlDialogVisible: false
      }
    },
    methods: {
      useredit(row) {
        this.$data.dialogTableVisibleUpdate = true
        this.$refs.updateUserInfo.toupdateusrInfo();
      },
      girlUseredit(row) {
        //alert("girlUseredit")
        this.$data.girlDialogVisible = true
      },
      userQuxiao() {
        this.$data.roleValue = [];
        //关闭串口
        this.$data.dialogTableVisible3 = false
      },
      userBaoCun() {
        this.$data.loading8 = true
        //数据
        let params = {}
        params.userId = this.$data.userId
        params.roleIds = this.$data.roleValue
        this.$axios.post(this.domain.serverpath + 'user/toBangDingRoleForUser', JSON.stringify(params)).then((response) => {
          //打开提示
          this.$notify.success({
            title: '提示',
            message: '保存成功！'
          });
          //关闭加载层
          this.$data.loading8 = false
          //清除数据
          this.$data.roleValue = [];
          //关闭弹出层
          this.$data.dialogTableVisible3 = false
          //刷新数据
          let pageInfo = {}
          pageInfo.pageNo = this.$refs.pageRef.currentPage
          this.toPage(pageInfo)
        })
      },
      remoteMethod(query) {//远程搜索
        this.$data.loading8 = true
        this.$axios.post(this.domain.serverpath + 'role/getRoleListByQuery', JSON.stringify({query: query})).then((response) => {
          this.$data.roleOptions = response.data.result
          this.$data.loading8 = false
        }).catch((error) => {
          this.$data.loading8 = false
        })
      },
      bandRoleForUser(row) {//给用户绑定角色
        this.$data.dialogTableVisible3 = true
        this.$data.userId = row.id
      },
      toPage(pageInfo) {//这是分页方法，每个分页的页面都要用该方法
        //有新的查询参数可以这么设置start
        pageInfo.userName = this.$data.formInline.userName
        pageInfo.startDate = this.$data.formInline.startDate
        pageInfo.endDate = this.$data.formInline.startDate
        pageInfo.sex = this.$data.formInline.sex
        //有新的查询参数可以这么设置end
        this.$axios.post(this.domain.serverpath + "/user/getUserPageList", JSON.stringify(pageInfo))
          .then((response) => {
            this.$data.tableData = response.data.result.userPage.resultList
            //给分页组件pagefoot设置总条数
            this.$refs.pageRef.total = response.data.result.userPage.totalCount
            this.$refs.pageRef.currentPage = response.data.result.userPage.pageNo

            //设置角色列表
            let rolelist = [];
            for (let i = 0; i < response.data.result.rolelist.length; i++) {
              let role = {};
              role.value = response.data.result.rolelist[i].name
              role.lable = response.data.result.rolelist[i].id
              rolelist.push(role)
            }
            this.$data.roleOptions = rolelist

          })
      },
      handleClick(row) {//
        alert(JSON.stringify(row))
      },
      addUser() {//添加新用户
        this.$data.dialogVisible = true
      },
      onSubmit() {//查询表单的提交
        let pageInfo = {};
        this.toPage(pageInfo)
      }
    },
    mounted() {
      //获取用户数据列表
      //打开数据加载动态图
      this.$data.loading = true;
      this.$axios.post(this.domain.serverpath + "user/getUserPageList", JSON.stringify()).then((response) => {
        this.$data.tableData = response.data.result.userPage.resultList
        //给分页组件设置总的记录数
        this.$refs.pageRef.total = response.data.result.userPage.totalCount
        //设置角色列表
        let rolelist = [];
        for (let i = 0; i < response.data.result.rolelist.length; i++) {
          let role = {};
          role.value = response.data.result.rolelist[i].name
          role.lable = response.data.result.rolelist[i].id
          rolelist.push(role)
        }
        this.$data.roleOptions = rolelist
        //关闭数据加载动图
        this.$data.loading = false;
      }).catch((err) => {
        //关闭数据加载动态图
        this.$data.loading = false;
        //弹消息窗口提示
        this.$notify.error({
          title: '错误',
          message: '获取数据出现问题'
        });
      })
    }
  }
</script>

<style scoped>

</style>
