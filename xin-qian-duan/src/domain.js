//全局的配置的一些服务端的域名
const serverpath='http://127.0.0.1:8888/'
//图片上传
const serverimageupload='http://'

const userinfo={};


/*关于试题管理模块*/
/*    新增试卷功能部分*/
//定义全局的试题类型变量
const shiTiLeiXing=[
  {label:'单选题',value:'1'},
  {label:'多选题',value:'2'},
  {label:'判断题',value:'3'},
  {label:'填空题',value:'4'},
  {label:'问答题',value:'5'}];
//定义全局的试题难度变量
const shiTiNanDu=["简单","一般","稍难","困难","汗颜难"];
//定义全局的试题状态
const shitizhuangtai=[
  {label:'关闭',value:'0'},
  {label:'开放',value:'1'}
];
//定义全局的给复选框提供的所有的选项
const xuanxiang = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];

/*关于试卷管理模块*/
/*创建试卷功能部分*/
//定义全局的试卷状态变量
const zhuangtai=[
  {label:'关闭',value:'0'},
  {label:'开放',value:'1'}
];

//定义是否公布答案变量
const havedaan=[
  {label:'否',value:'0'},
  {label:'是',value:'1'}
];

//定义试卷的显示形式的变量
const xianshixingshi=[
  {label:'整卷展示',value:'0'},
  {label:'单题展示',value:'1'}
];

//定义试卷类型的变量
const shijuanleixing=[
  {label:'普通试卷',value:'0'},
  {label:'随机试卷',value:'1'}
];

//定义试题顺序的变量
const shitishunxu=[
  {label:'原始顺序',value:'0'},
  {label:'随机顺序',value:'1'}
];


export default {
  serverpath,
  userinfo,
  shiTiLeiXing,
  shiTiNanDu,
  shitizhuangtai,
  xuanxiang,
  zhuangtai,
  havedaan,
  xianshixingshi,
  shijuanleixing,
  shitishunxu
}

