<template>
  <!--创建试卷start-->
  <div :style="{height:wheight+'px'}">

    <!--创建试卷start-->
    <el-form size="mini" :model="formInline" :rules="rules" label-width="130px" ref="formInline"
             class="demo-form-inline">

      <el-form-item label="试卷名称：" prop="shijuanname">
        <el-input style="width: 400px;" v-model="formInline.shijuanname" placeholder="原创"></el-input>
      </el-form-item>

      <el-col :span="11">
        <el-form-item label="试卷分类：" prop="fenleiid">
          <el-select v-model="formInline.fenleiid">
            <el-option v-for="(item,index) in fenleiList" :key="index" :label="item.shijuanfenlei" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2"></el-col>
      <el-col :span="11">
        <el-form-item label="试卷状态：" prop="zhuangtai">
          <el-select v-model="formInline.zhuangtai">
            <el-option v-for="(item,index) in zhuangtai" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="11">
        <el-form-item label="开考时间：" prop="starttime">
          <el-date-picker v-model="formInline.starttime" type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd hh:mm:ss" style="width: 200px;"></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2"></el-col>
      <el-col :span="11">
        <el-form-item label="结束时间：" prop="endtime">
          <el-date-picker v-model="formInline.endtime" type="datetime" placeholder="选择日期" value-format="yyyy-MM-dd hh:mm:ss" :start-placeholder="formInline.starttime" style="width: 200px;"></el-date-picker>
        </el-form-item>
      </el-col>

      <el-col :span="11">
        <el-form-item label="成绩公布时间：" prop="opentime">
          <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd hh:mm:ss" v-model="formInline.opentime"
                          style="width: 200px;"></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2"></el-col>
      <el-col :span="11">
        <el-form-item label="考试时长：" prop="shijian">
          <el-input style="width: 200px;" v-model="formInline.shijian" :placeholder="formInline.starttime-formInline.endtime"></el-input>
        </el-form-item>
      </el-col>

      <el-col :span="11">
        <el-form-item label="公布答案：" prop="havedaan">
          <el-select v-model="formInline.havedaan">
            <el-option v-for="(item,index) in havedaan" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2"></el-col>
      <el-col :span="11">
        <el-form-item label="显示形式：" prop="xianshixingshi">
          <el-select v-model="formInline.xianshixingshi">
            <el-option v-for="(item,index) in xianshixingshi" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="11">
        <el-form-item label="试卷类型：" prop="shijuanleixing">
          <el-select v-model="formInline.shijuanleixing">
            <el-option v-for="(item,index) in shijuanleixing" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2"></el-col>
      <el-col :span="11">
        <el-form-item label="试题排序顺序：" prop="shitishunxu">
          <el-select v-model="formInline.shitishunxu">
            <el-option v-for="(item,index) in shitishunxu" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="试卷说明：" prop="shuoming">
          <el-input type="textarea" v-model="formInline.shuoming" style="width: 756px"></el-input>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <div>
          <el-form-item label="目标用户：">
            <el-button type="info" plain @click="checkUser">选择用户</el-button>
            <el-button type="info" plain>导入</el-button>
            <el-button type="info" plain>清空</el-button>
          </el-form-item>
          <div style="padding-left: 130px">
            <h v-if="checkedInfo != null">
              <h v-for="(item,index) in checkedInfo" :key="index">{{item.userName}}&nbsp;&nbsp;&nbsp;</h>  <!--遍历被选中的用户的id-->
            </h>
          </div>
        </div>
      </el-col>


      <el-col :span="24">
        <el-form-item label="" prop="tixingid">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-col>
    </el-form>
    <!--创建试卷end-->


    <!--选择用户弹框start-->
    <el-dialog
      title="选择"
      :visible.sync="dialogVisible2"
      width="70%"
      :before-close="handleClose">

      <xuanzeyonghu ref="xuanzeyonghu"></xuanzeyonghu>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2=false">取 消</el-button>
        <el-button type="primary" @click="piliangcharu">批量插入</el-button>
      </span>
    </el-dialog>
    <!--选择用户弹框end-->

  </div>
  <!--创建试卷end-->
</template>

<script type="text/ecmascript-6">
  import xuanzeyonghu from './xuanzeyonghu'

  export default {
    name: "createShiJuan",
    components: {
      xuanzeyonghu
    },
    data() {
      return {
        dialogVisible:false,
        dialogVisible2:false,
        wheight: window.innerHeight - 132,
        fenleiList: [],
        zhuangtai: this.domain.zhuangtai,
        havedaan: this.domain.havedaan,
        xianshixingshi: this.domain.xianshixingshi,
        shijuanleixing: this.domain.shijuanleixing,
        shitishunxu: this.domain.shitishunxu,
        formInline: {},
        checkedInfo:[],
        rules: {    //表单验证
          shijuanname: [
            {required: true, message: '请添加试卷名称', trigger: 'blur'},
          ],
          fenleiid: [
            {required: true, message: '请选择试卷分类', trigger: 'change'}
          ],
          zhuangtai: [
            {required: true, message: '请选择试卷状态', trigger: 'change'}
          ],
          starttime: [
            {required: true, message: '请填写开考时间', trigger: 'blur'}
          ],
          endtime: [
            {required: true, message: '请填写结束时间', trigger: 'change'}
          ],
          opentime: [
            {required: true, message: '请填写成绩公布时间', trigger: 'change'}
          ],
          shijian: [
            {required: true, message: '请填写考试时长', trigger: 'change'}
          ],
          havedaan: [
            {required: true, message: '请选择是否公布答案', trigger: 'change'}
          ],
          xianshixingshi: [
            {required: true, message: '请选择试卷的显示形式', trigger: 'change'}
          ],
          shijuanleixing: [
            {required: true, message: '请选择试卷类型', trigger: 'change'}
          ],
          shitishunxu: [
            {required: true, message: '请选择试题的排序顺序', trigger: 'change'}
          ],
          shuoming: [
            {required: true, message: '请填写试卷说明', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
       handleClose(){//before-close	关闭前的回调，会暂停 Dialog 的关闭

      },
      piliangcharu(){
         this.$data.dialogVisible2 = false;
         this.$data.checkedInfo = this.$refs.xuanzeyonghu.checkedInfo;
      },
      onSubmit() {
        this.$refs['formInline'].validate((valid) => {
          if (valid) {
            //将需要考试的人名信息添加到formInline对象中
            let userid = [];
            for (let i = 0; i <this.$data.checkedInfo.length ; i++) {
              userid.push(this.$data.checkedInfo[i].id);
            }

            this.$data.formInline.checkedInfo = JSON.stringify(userid);

            let qs = require("qs");
            //向后台发送数据创建试卷
            this.$axios.post(this.domain.serverpath+"tiku2/createKaoShiShiJuan",qs.stringify(this.$data.formInline)).then((response)=>{
              //创建成功
              this.$data.formInline = {};
              this.$data.checkedInfo = [];
              this.$message({
                message: '恭喜你，添加成功',
                duration: 1500,
                type: 'success'
              });
            }).catch((error)=>{
              //创建失败
              this.$message({
                message: '添加失败',
                duration: 1500,
                type: 'error'
              });
            })

          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      getfenleiList() { //加载试卷分类下拉列表
        this.$axios.post(this.domain.serverpath + "tiku2/getfenleiList").then((response) => {
          if (response.data.success == "ok") {
            this.$data.fenleiList = response.data.result;
          }
        })
      },
      checkUser() {
        this.$data.dialogVisible = true;
        this.$data.dialogVisible2 = true;
      }
    },
    mounted() {
      this.getfenleiList();   //加载试卷分类下拉菜单
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
