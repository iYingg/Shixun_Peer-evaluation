<template>
    <div class="app-container">
        <h1 class="title">您所教授的课程:</h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入课程名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>

        </el-row>

        <el-table :data="tableData" stripe style="width: 100%" >
            <el-table-column prop="cno" label="课程号" width="250" >
            </el-table-column>
            <el-table-column prop="cname" label="课程名" width="250" />
            <el-table-column prop="hours" label="课时" width="300" />
            <el-table-column prop="count" label="学生数量" width="300" />

            <el-table-column prop="action" label="操作" width="180" >
                <template v-slot="scope" >
                    <el-button type="success" @click="chooseCourse(scope.row.cno,scope.row.cname)" >进入</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                v-model:currentPage="queryForm.pageNum"
                v-model:page-size="queryForm.pageSize"
                :page-sizes="[10, 20, 30, 40]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
        />
    </div>

</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";


import { ref } from 'vue'
import router from "@/router";

const tableData=ref([]);

const total=ref(0)

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:''
})


/**
 * 初始化列表
 * @returns {Promise<void>}
 */

const initUserList=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/courseTea/listTeaching",queryForm.value);
    tableData.value=res.data.courseList;
    total.value=res.data.total;
}

initUserList();


const handleSizeChange=(pageSize)=>{
    queryForm.value.pageNum=1;
    queryForm.value.pageSize=pageSize;
    initUserList()
}

const handleCurrentChange=(pageNum)=>{
    queryForm.value.pageNum=pageNum;
    initUserList()
}


/**
 * 跳转
 * @param cno
 * @param cname
 */
const chooseCourse =(cno,cname)=>{

    let route = {
        name:'学生列表:'+cname,
        path:`/home/activ/stuCourse${cno}`,
        meta:{
            parentName:'学生列表'
        },
    }

    route.component = () => import('@/views/activ/stuCourse.vue');

    router.addRoute('首页',route);

    sessionStorage.setItem("cno",cno);
    sessionStorage.setItem("cname",cname);
    router.push({
        path:`/home/activ/stuCourse${cno}`,
    });
}

</script>


<style lang="scss" scoped>
.title{
  font-size: x-large;
  font-weight: bold;
}

.header{
  padding-top: 20px;
  padding-bottom: 16px;
  box-sizing: border-box;
}
.el-pagination{
  float: right;
  padding: 20px;
  box-sizing: border-box;
}
::v-deep th.el-table__cell{
  word-break: break-word;
  background-color: #f8f8f9 !important;
  color: #515a6e;
  height: 40px;
  font-size: 15px;
}
.el-tag--small {
  margin-left: 5px;
}
</style>