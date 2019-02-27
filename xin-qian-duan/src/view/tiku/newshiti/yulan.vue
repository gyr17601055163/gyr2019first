<template>
  <div>
    <!--预览试卷弹框start-->
    <el-form :model="form">
      <el-form-item label="试题类型：">{{form.tixingid}}</el-form-item>
      <el-form-item label="试题难度：">{{form.nanduid}}</el-form-item>
      <el-form-item label="所属题库：">{{form.tikuname}}</el-form-item>
      <el-form-item label="试题状态：">{{form.shitizhuangtai}}</el-form-item>
      <el-form-item label="试题题干：">{{form.tigan}}</el-form-item>
      <el-form-item label="选项设置：">{{form.xuanxiang}}--{{form.xuanxiangmiaoshu}}</el-form-item>
      <el-form-item label="试题答案：">{{form.daan}}</el-form-item>
      <el-form-item label="答案解析：">{{form.fenxi}}</el-form-item>
    </el-form>
    <!--预览试卷弹框end-->
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "yulan",
    data() {
      return {
        form:{}
      }
    },
    methods:{
      updateHuiXian(row){
        this.$data.form = row;
      }
    },
    mounted(){
      this.$emit("yulanInfo");
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
