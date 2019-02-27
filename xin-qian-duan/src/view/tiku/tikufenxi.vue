<template>
  <div>

    <!--start-->
    <el-row type="flex" class="row-bg" justify="space-between">
      <el-col :span="6">
        <div class="grid-content bg-purple">题库名称：</div>
      </el-col>
      <el-col><div class="neirong">{{tikuname}}</div></el-col>
    </el-row>
    <el-row type="flex" class="row-bg" justify="space-between">
      <el-col :span="6">
        <div class="grid-content bg-purple">状态：</div>
      </el-col>
      <el-col><div class="neirong">{{tikuzhuangtia}}</div></el-col>
    </el-row>
    <!--end-->

    <br>
    <br>

    <!--饼状图start-->
    <div id="bingzhuangtu" style="height:400px">
    </div>
    <!--饼状图end-->

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "tikufenxi",
    data() {
      return {
        tikuname:'',
        tikuzhuangtia:'',
        tubiaotext:'',
        tubiaodata:[{value: 335, name: '直接访问'},
          {value: 310, name: '邮件营销'},
          {value: 234, name: '联盟广告'},
          {value: 135, name: '视频广告'},
          {value: 1548, name: '搜索引擎'}],
        tubiaoshowdadta:['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
      }
    },
    methods: {
      showTuBiao() {    //显示饼状图
        var echarts = require('echarts');

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('bingzhuangtu'));

        let option = {
          title: {
            text: this.$data.tubiaotext,
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: this.$data.tubiaoshowdata
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data:this.$data.tubiaodata,
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };

        // 绘制图表
        myChart.setOption(option);
      }
    },
    mounted() {
      this.showTuBiao();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .el-row {
    margin-bottom: 1px;
    &:last-child {
      margin-bottom: 0;
    }
  }

  .bg-purple-dark {
    background: #99a9bf;
  }

  .bg-purple {
    background: #d3dce6;
  }

  .grid-content {
    border-radius: 4px;
    min-height: 25px;
    line-height: 25px
    text-align: right
    font-size: 12px
  }

  .row-bg {
    padding: 0px 0;
    background-color: #f9fafc;
  }
  .neirong{
    border-radius: 4px;
    min-height: 25px;
    line-height: 25px
    text-align: center
    font-size: 12px
  }
</style>
