<template>
  <div>

    <!--新增题库表单start-->
    <el-form ref="form" :model="form" label-width="80px" :style="{width:'60%'}">
      <el-form-item label="题库ID:">
        <el-input v-model="form.id"></el-input>
      </el-form-item>
      <el-form-item label="题库名称:">
        <el-input v-model="form.tikuname"></el-input>
      </el-form-item>
      <el-form-item label="状态:">
        <el-select v-model="form.tikuzhuangtai" placeholder="关闭状态下不允许存取试题">
          <el-option label="开放" value="开放"></el-option>
          <el-option label="关闭" value="关闭"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="题库说明:">
        <el-input type="textarea" v-model="form.tikushuoming"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交页面</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
    <!--新增题库表单end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "tikuxiugai",
    data() {
      return {
        dialogVisible2:true,
        form: {
          id: '',
          tikuname: '',
          tikuzhuangtai: '',
          tikushuoming: ''
        }
      }
    },
    methods: {
      updatehuixian(row){   //数据回显
        this.$data.form=row
      },
      onSubmit() {
        let formInfo = this.$data.form;
        let qs = require("qs");

        this.$axios.post(this.domain.serverpath + "tiku/updateTiKu", qs.stringify(formInfo)).then((response) => {
          //关闭修改窗口start
          if(this.$data.dialogVisible2){
            this.$data.dialogVisible2 = false;
          }
          this.$emit("tikuxiugai9",this.$data.dialogVisible2); //关闭修改窗口
          //关闭修改窗口end

          //清空表单中的数据
          this.form = {};

          //提示添加成功
          this.$message({
            message: '恭喜你，修改成功',
            duration: 1500,
            type: 'success'
          });

          //this.$emit(tikuxiugai2);

        }).catch((error) => {
          this.$message({
            message: '修改失败',
            duration: 1500,
            type: 'error'
          });
        })
      },

    },
    mounted(){
      this.$emit("tikuxiugai9",this.$data.dialogVisible2); //初始化回显方法
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
