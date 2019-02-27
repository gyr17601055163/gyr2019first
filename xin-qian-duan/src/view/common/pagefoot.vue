<template>
  <el-pagination
    background
    :current-page="currentPage"
    layout="sizes,prev, pager, next"
    :page-sizes="[10, 20, 30, 40]"
    :page-size="10"
    :total="total"
    @size-change="pageSizeChange"
    @current-change="currentPageChange"
    >
  </el-pagination>
</template>

<script>
    export default {
      name:"pagefoot",
      data(){
        return{
          currentPage:1,
          total:0,
          pageNo:1,
          pageSize:10
        }
      },
      methods:{
        setPageData(total){//用来让调用分页组件的页面获取分页组件页面的值
          this.$data.total=total
          return {pageNo:this.$data.pageNo,pageSize:this.$data.pageSize}
        },
        pageSizeChange(size){//当改变页面大小的时候触发
          //size表示所选中的每页的显示条数
          this.$data.pageSize=size;
          //分页组件页面调用父页面的方法
          this.$emit('toforpage',{pageNo:this.$data.pageNo,pageSize:this.$data.pageSize});
        },
        currentPageChange(pageNo){//当改变页码的时候触发
          //pageNo表示所选中的每页的显示条数
          this.$data.pageNo=pageNo;
          //分页组件页面调用父页面的方法
          this.$emit('toforpage',{pageNo:this.$data.pageNo,pageSize:this.$data.pageSize});
        },


      }
    }
</script>

<style scoped>

</style>
