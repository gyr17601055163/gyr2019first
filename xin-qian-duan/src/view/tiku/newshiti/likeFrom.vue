<template>
  <!--管理试卷页面的模糊查寻表单-->
  <div>

    <!--模糊表单start-->
    <el-form :inline="true" :model="formInline" label-width="70px" class="demo-form-inline" size="mini">
      <el-form-item label="关键词：">
        <el-input v-model="formInline.tigan" placeholder="试题题干查寻"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuid" placeholder="所属题库" style="width: 130px">
          <el-option v-for="(item,index) in tikus" :key="index" :label="item.tikuname" :value="item.tikuname"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tixingid" placeholder="试题类型" style="width: 130px">
          <el-option v-for="(item,index) in shiTiLeiXing" :key="index" :label="item.label" :value="item.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.nanduid" placeholder="试题难度" style="width: 130px">
          <el-option v-for="(item,index) in shiTiNanDu" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.shitizhuangtai" placeholder="试题状态" style="width: 130px">
          <el-option v-for="(item,index) in shitizhuangtai" :key="index" :label="item.label" :value="item.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-select v-model="formInline.tikuzhuangtai" placeholder="排序方式" style="width: 130px">
          <el-option label="更新中..." value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <!--模糊表单end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "likeFrom",
    data() {
      return {
        tikus: [],  //题库
        shiTiLeiXing: this.domain.shiTiLeiXing,    //试题类型
        shiTiNanDu: this.domain.shiTiNanDu,        //试题难度
        shitizhuangtai:this.domain.shitizhuangtai,  //试题状态
        formInline: {
          tigan:'',
          tikuid:null,
          tixingid:'',
          nanduid:'',
          shitizhuangtai:''
        }
      }
    },
    methods: {
      onSubmit() { //模糊查寻的表单提交方法
        this.$emit("likeFrom",
          {tigan:this.$data.formInline.tigan,tikuid:this.$data.formInline.tikuid,tixingid:this.$data.formInline.tixingid,
            nanduid:this.$data.formInline.nanduid,shitizhuangtai:this.$data.formInline.shitizhuangtai});
      },
      getTikusToRedis() {  //从Redis中获取题库信息
        this.$axios.post(this.domain.serverpath + "tiku2/getTiKusToRedis").then((response) => {
          this.$data.tikus = response.data.result
        }).catch((error) => {
          this.$notify({
            title: '警告',
            message: '没有获取到可用的题库信息！',
            type: 'warning'
          });
        })
      }
    },
    mounted() {
      this.getTikusToRedis(); //加载Redis中的题库
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
