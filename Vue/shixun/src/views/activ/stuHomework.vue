<template>
    <div class="app-container">
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入用户名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="sno" label="学号" width="350" />
            <el-table-column prop="sname" label="姓名" width="350" />
            <el-table-column prop="finalscore" label="最终成绩" width="350" >
                <template v-slot="scope">
                    <el-text v-if="scope.row.finalscore == -1">暂无成绩</el-text>
                    <el-text v-else>{{scope.row.finalscore}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="action" label="操作" width="380" >
                <template v-slot="scope" >
                    <el-button type="primary" @click="edit(scope.row.sno,scope.row.finalscore)" >修改最终成绩</el-button>
                    <el-button type="warning" @click="check(scope.row.sno,scope.row.finalscore)" >查看</el-button>
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
import {ElMessage} from "element-plus";
import router from "@/router";

const tableData=ref([]);

const total=ref(0)

const dialogVisible=ref(false)

const dialogTitle=ref("")

const id=ref(-1)

const no=ref("")

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:'',
    cno:'',
    hid:''
})
const qcno = ref(sessionStorage.getItem("cno"))
const qhid = ref(sessionStorage.getItem("hid"));
const initUserList=async()=>{
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    queryForm.value.hid = qhid.value;
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/stu/list",queryForm.value);
    tableData.value=res.data.userList;
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

const edit=(sno,finalscore)=>{
    let cname = sessionStorage.getItem("cname")
    let cno = sessionStorage.getItem("cno")
    let hid = sessionStorage.getItem("hid")
    sessionStorage.setItem("finalscore",finalscore)
    sessionStorage.setItem("sno",sno)
    let route = {
        name:'学生列表:'+ cname + hid +sno+"edit",
        path:`/home/activ/scoreCourse${cno}${hid}${sno}edit`,
        meta:{
            parentName:'学生列表:'+cname+hid
        },
    }

    route.component = () => import('@/views/activ/stuScoreedit.vue');

    router.addRoute('首页',route);


    router.push({
        path:`/home/activ/scoreCourse${cno}${hid}${sno}edit`,
    });
}

const check=(sno,finalscore)=>{
    let cname = sessionStorage.getItem("cname")
    let cno = sessionStorage.getItem("cno")
    let hid = sessionStorage.getItem("hid")
    sessionStorage.setItem("finalscore",finalscore)
    sessionStorage.setItem("sno",sno)
    let route = {
        name:'学生列表:'+ cname + hid +sno,
        path:`/home/activ/scoreCourse${cno}${hid}${sno}`,
        meta:{
            parentName:'学生列表:'+cname+hid
        },
    }

    route.component = () => import('@/views/activ/stuScore.vue');

    router.addRoute('首页',route);


    router.push({
        path:`/home/activ/scoreCourse${cno}${hid}${sno}`,
    });
}



</script>


<style lang="scss" scoped>
.header{
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