<template>
  <div>
    <p>菜单列表</p>
    <el-input :style="{width:'50%'}"
              placeholder="输入关键字进行过滤"
              v-model="filterText">
    </el-input>

    <el-tree
      class="filter-tree"
      :data="data2"
      :props="defaultProps"
      default-expand-all
      show-checkbox
      :filter-node-method="filterNode"
      ref="tree2"
      @node-click="nodeClick"
      @node-contextmenu="youjishijian"
    >
    </el-tree>

    <el-dialog
      :title="menuInfo"
      :visible.sync="dialogVisible"
      width="30%"

      v-loading="loading2"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    >
      <el-row style="border-bottom: darkgrey 1px solid;margin-bottom:10px;">
        <el-tooltip class="item" effect="dark" content="点击添加菜单" placement="top-start">
          <el-button icon="el-icon-plus" circle @click="add"></el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="点击更新当前信息" placement="top-start">
          <el-button type="primary" icon="el-icon-edit" circle @click="update2"></el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="删除当前菜单" placement="top-start">
          <el-button type="danger" icon="el-icon-delete" circle @click="deleteMenu"></el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="点击添加一级菜单" placement="top-start">
          <el-button type="warning" icon="el-icon-star-off" circle @click="addFirstMenu"></el-button>
        </el-tooltip>
      </el-row>


      <el-form ref="add" v-show="form1.form1show" :inline="true" v-model="form1" class="demo-form-inline">
        <i class="el-icon-plus" style="margin-top:10px">添加菜单</i>
        <br>
        <br>
        <el-form-item label="当前菜单等级">
          <el-input v-model="form1.parentLeval" :style="{'margin-left':15+'px',width:200+'px'}"
                    :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单名称">
          <el-input v-model="form1.parentName" :style="{'margin-left':15+'px',width:200+'px'}"
                    :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单ID">
          <el-input v-model="form1.parentId" :style="{'margin-left':29+'px',width:200+'px'}"
                    :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="新添加子菜单名称">
          <el-input v-model="form1.currMenuName" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="子菜单访问的URL">
          <el-input v-model="form1.currMenuUrl" maxlength="10"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :plain="true" @click="saveMenu()">保存</el-button>
          <el-button @click="form2QuXiao">取消</el-button>
        </el-form-item>
      </el-form>

      <!--修改表单start-->
      <el-form ref="update" v-show="form2.form2show" :inline="true" v-model="form2" class="demo-form-inline">
        <i class="el-icon-edit">修改菜单信息</i>
        <br>
        <br>
        <el-form-item label="当前菜单等级">
          <el-input v-model="form2.leval" :style="{width:200+'px'}" :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单ID">
          <el-input v-model="form2.Id" :style="{'margin-left':14+'px',width:200+'px'}"
                    :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单的父ID">
          <el-input v-model="form2.parentId" :style="{'margin-left':14+'px',width:200+'px'}"
                    :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单名称">
          <el-input v-model="form2.menuName" :style="{width:200+'px'}"></el-input>
        </el-form-item>
        <el-form-item label="当前菜单访问的URL">
          <el-input v-model="form2.menuUrl" :style="{width:200+'px'}"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="form2Submit">保存修改</el-button>
          <el-button @click="form2QuXiao">取消</el-button>
        </el-form-item>
      </el-form>
      <!--修改表单end-->

      <el-form ref="addFirstMenu" v-show="addFirst.form1show" :inline="true" v-model="addFirst"
               class="demo-form-inline">
        <i class="el-icon-plus" style="margin-top:10px">添加一级菜单</i>
        <br>
        <br>
        <el-form-item label="新添加菜单名称">
          <el-input v-model="addFirst.currMenuName" maxlength="10"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :plain="true" @click="saveFirstMenu()">保存</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>

      <!--<span slot="footer" class="dialog-footer">
         <el-button @click="dialogVisible = false">取 消</el-button>
         <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>-->
    </el-dialog>

  </div>
</template>

<script>
  export default {
    name: "menuManger",
    data() {
      return {
        form1: {
          parentLeval: '',
          parentName: '',
          parentId: '',
          currMenuName: '',
          currMenuUrl: '',
          form1show: false
        },
        form2: {
          leval: '',
          parentId: '',
          Id: '',
          menuName: '',
          menuUrl: '',
          form2show: false,
        },
        addFirst: {
          currMenuName: '',
          form1show: false
        },
        parent: {
          id: '',
          leval: ''
        },
        menuInfo: "",
        dialogVisible: false,
        loading2: false,  //加载效果默认不显示
        filterText: '',
        data2: [],
        defaultProps: {
          id: 'id',
          children: 'listMenu',
          label: 'label',
          leval: 'leval',
          parentMenuId: 'parentMenuId',
        }
      }
    },
    watch: {
      filterText(val) {
        this.$refs.tree2.filter(val);
      }
    },
    methods: {
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      youjishijian(data) {

      },
      nodeClick(node) {
        //弹出层
        this.$data.dialogVisible = true;
        this.$data.form1.form1show = false;
        this.$data.form2.form2show = false;

        this.$data.addFirst.form1show = false;
        this.$data.menuInfo = "当前操作的菜单名称-" + node.label;
        this.$data.form1.parentId = node.id;
        this.$data.form1.parentName = node.menuName;
        this.$data.form1.parentLeval = node.leval;

        this.$data.form2.Id = node.id;
        this.$data.form2.leval = node.leval;
        this.$data.form2.parentId = node.parentMenuId;
        this.$data.form2.menuName = node.menuName;
        this.$data.form2.menuUrl = node.url;
      },
      add() {
        if (this.$data.form2.form2show) {
          this.$data.form2.form2show = false;
        }
        if (!this.$data.form1.form1show) {
          this.$data.form1.form1show = true;
        }
        if (this.$data.addFirst.form1show) {
          this.$data.addFirst.form1show = false;
        }
      },
      update2() {
        if (this.$data.form1.form1show) {
          this.$data.form1.form1show = false;
        }
        if (!this.$data.form2.form2show) {
          this.$data.form2.form2show = true;
        }
        if (this.$data.addFirst.form1show) {
          this.$data.addFirst.form1show = false;
        }
      },
      addFirstMenu() { //添加一级菜单按钮
        if (this.$data.form1.form1show) {
          this.$data.form1.form1show = false;
        }
        if (this.$data.form2.form2show) {
          this.$data.form2.form2show = false;
        }
        if (!this.$data.addFirst.form1show) {
          this.$data.addFirst.form1show = true;
        }

      },
      saveMenu() {
        //打开加载效果
        this.$data.loading2 = true;
        //保存新增加的菜单数据
        let form1 = this.$data.form1;
        //获取查询参数
        let qs = require("qs");
        this.$axios.post(this.domain.serverpath + "menu/addMenu", qs.stringify(form1)).then((response) => {
          //收到数据库的响应，关闭加载效果，添加成功或者失败再给出相应的提示
          this.$data.loading2 = false;
          if (response.data != null && response.data.success == "ok") {
            //1、清空表单
            this.$data.form1 = {form1show: false};  //其他属性的值是空，而且表单是关闭的

            this.$message('添加成功');

            //2、关闭操作的窗口
            this.$data.dialogVisible = false;

            //3、刷新数据
            this.$axios.post(this.domain.serverpath + "menu/treeMenu").then((reponse) => {
              //为data中的listMenu赋值
              this.data2 = reponse.data.result;

            }).catch((error) => {
              console.log("===error===" + error.message);

            })
          }
        }).catch((error) => {
          //如果报错，也关闭加载效果，给出相应提示
          this.$data.loading2 = false;
          this.$notify.error({
            title: '错误',
            message: '出错了，请联系管理员'
          });
        })
      },
      saveFirstMenu() {
        //打开加载效果
        this.$data.loading2 = true;
        //保存新增加的菜单数据
        let addFirst = this.$data.addFirst;
        //获取查询参数
        let qs = require("qs");
        this.$axios.post(this.domain.serverpath + "/menu/addMenu", qs.stringify(addFirst)).then((response) => {
          //收到数据库的响应，关闭加载效果，添加成功或者失败再给出相应的提示
          this.$data.loading2 = false;
          if (response.data != null && response.data.success == "ok") {
            //1、清空表单
            this.$data.addFirst = {form1show: false};  //其他属性的值是空，而且表单是关闭的

            this.$message('添加成功');

            //2、关闭操作的窗口
            this.$data.dialogVisible = false;

            //3、刷新数据
            this.$axios.post(this.domain.serverpath + "menu/treeMenu").then((reponse) => {
              //为data中的listMenu赋值
              this.data2 = reponse.data.result;

            }).catch((error) => {
              console.log("===error===" + error.message);

            })
          }
        }).catch((error) => {
          //如果报错，也关闭加载效果，给出相应提示
          this.$data.loading2 = false;
          this.$notify.error({
            title: '错误',
            message: '出错了，请联系管理员'
          });
        })
      },
      form2Submit() {    //保存修改
        //打开加载效果
        this.$data.loading2 = true;
        //获取表单中的数据
        let formInfo = this.$data.form2;
        //获取查询参数
        let qs = require("qs");

        this.$axios.post(this.domain.serverpath + "menu/updateMenu", qs.stringify(formInfo)).then((response) => {
          //收到数据库的响应，关闭加载效果，添加成功或者失败再给出相应的提示
          this.$data.loading2 = false;
          if (response.data != null && response.data.success == "ok") {
            //1、清空表单
            this.$data.form2 = {form2show: false};  //其他属性的值是空，而且表单是关闭的

            this.$message('修改成功');

            //2、关闭操作的窗口
            this.$data.dialogVisible = false;

            //3、刷新数据
            this.$axios.post(this.domain.serverpath + "menu/treeMenu").then((reponse) => {
              //为data中的listMenu赋值
              this.data2 = reponse.data.result;

            }).catch((error) => {
              console.log("===error===" + error.message);

            })
          }
        }).catch((error) => {
          this.$data.loading2 = false;
          this.$notify.error({
            title: '错误',
            message: '出错了，请联系管理员'
          });
        })
      },
      form2QuXiao() {
        this.$data.dialogVisible = false;
      },
      deleteMenu() { //删除目录
        if (this.$data.form1.form1show) {
          this.$data.form1.form1show = false;
        }
        if (this.$data.form2.form2show) {
          this.$data.form2.form2show = false;
        }
        if (this.$data.addFirst.form1show) {
          this.$data.addFirst.form1show = false;
        }
        //打开加载效果
        this.$data.loading2 = true;
        //获取查询参数
        let qs = require("qs");
        let formInfo = this.$data.form2

        this.$axios.post(this.domain.serverpath + "menu/deleteMenu",qs.stringify(formInfo)).then((reponse) => {
          //收到数据库的响应，关闭加载效果，添加成功或者失败再给出相应的提示
          this.$data.loading2 = false;
          if (response.data != null && response.data.success == "ok") {
            this.$message('删除成功');

            //2、关闭操作的窗口
            this.$data.dialogVisible = false;

            //3、刷新数据
            this.$axios.post(this.domain.serverpath + "menu/treeMenu").then((reponse) => {
              //为data中的listMenu赋值
              this.data2 = reponse.data.result;

            }).catch((error) => {
              console.log("===error===" + error.message);

            })
          }
        }).catch((error) => {
          this.$data.loading2 = false;
          this.$notify.error({
            title: '错误',
            message: '出错了，请联系管理员'
          });
        })
      }
    },
    mounted() {
      this.$axios.post(this.domain.serverpath + "menu/treeMenu").then((reponse) => {
        //为data中的listMenu赋值
        this.data2 = reponse.data.result;
      }).catch((error) => {
        console.log("===error===" + error.message);
      })
    }
  }
</script>

<style scoped>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

</style>
