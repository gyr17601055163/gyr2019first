<template>
  <div>

    <el-tabs type="border-card">
      <el-tab-pane label="成绩分析">
        <el-form ref="formInline" :model="formInline" label-width="150px">
          <el-form-item label="选择试卷：">
            <el-select v-model="formInline.name" filterable placeholder="请选择">
              <el-option label="" value="">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="分数区分：">
            <div>
              <div style="padding-top:5px;padding-left: 5px">
                <el-button type="primary" @click="createyuansu" size="mini">增加区间</el-button>
              </div>

              <div>
                <el-row>
                  <el-col :span="20"><!-- 分数区间代码 -->
                    <div class="grid-content" style="padding-top:5px;">
                      <div>
                        <div v-for="(item,index) in array" :key="index" style="width:80%">
                          <el-row>
                            从
                            <el-input size="mini" v-model.number="fenshu1[index]" placeholder="请输入区间分数"
                                      style="width:10%"></el-input>
                            至
                            <el-input size="mini" v-model.number="fenshu2[index]" placeholder="请输入区间分数"
                                      style="width:10%"></el-input>
                            <el-button size="mini" type="danger" icon="el-icon-delete" circle
                                       @click="delindex(index)"></el-button>
                          </el-row>
                        </div>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="4">
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-form-item>
          <el-form-item size="large">
            <el-button type="primary" size="medium" @click="onSubmit">开始统计</el-button>
            <el-button size="medium">取消</el-button>
          </el-form-item>
        </el-form>

        <!--饼状图start-->

        <br>
        <br>
        <el-row>
          <div style="padding-left:30%">
            <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
            <div id="main" style="width: 500px;height: 500px"></div>
          </div>
        </el-row>

        <!--饼状图end-->

      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: "scoreFenXi",
    data() {
      return {
        fenshu1: [0],
        fenshu2: [0],
        array: [{value: " "}],
        bingdata:[{name:" ",value:0}],//饼图的数据
        bingtextdata:[" "],//饼图的项的数据
        formInline: {},
        rules: {
          laiyuan: [
            {required: true, message: '请选择试题类型', trigger: 'blur'},
          ]
        }
      }
    },
    methods: {
      createyuansu() {//添加区间成绩
        let uu = {value: "B"}
        this.$data.fenshu1.push(0)
        this.$data.fenshu2.push(0)
        this.$data.array.push(uu)
      },
      delindex(index) {//删除一个添加的区间
        let uu = []
        this.$data.array.splice(index, 1)
        this.$data.fenshu1.splice(index, 1)
        this.$data.fenshu2.splice(index, 1)
      },
      chushihuabingtu() {
        var echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        // 绘制图表
        myChart.setOption({
          title : {text:'成绩分析图',subtext: '',x:'center'},
          tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"},
          legend: {orient: 'vertical',left: 'left',data:this.$data.bingtextdata},
          series : [{
            name:'人数/百分比',type: 'pie',radius : '55%',center: ['50%', '60%'],
            data:this.$data.bingdata,
            itemStyle: {emphasis: {shadowBlur:10,shadowOffsetX:0,shadowColor: 'rgba(0, 0, 0, 0.5)'}
            }
          }]
        });
      }
    },
    mounted() {
      this.chushihuabingtu();
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
