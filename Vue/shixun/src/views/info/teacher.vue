<template>
    <div class="app-container">
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入用户名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
            <el-button type="success"  @click="handleDialogValue()">新增
            </el-button>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="no" label="工号" width="250" />
            <el-table-column prop="name" label="姓名" width="250" />
            <el-table-column prop="phone" label="电话" width="300" />
            <el-table-column prop="email" label="邮箱" width="300" />
            <el-table-column prop="action" label="操作" width="180" >
                <template v-slot="scope" >
                    <el-button type="primary" @click="handleDialogValue(scope.row.no)" >修改</el-button>
                    <el-popconfirm title="您确定要删除这条记录吗？" @confirm="handleDelete(scope.row.no)">
                        <template #reference>
                            <el-button type="danger" >删除</el-button>
                        </template>
                    </el-popconfirm>
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
    <Dialog :modelValue="dialogVisible" :dialogVisible="dialogVisible" :id="id" :no="no"
            :dialogTitle="dialogTitle" @initUserList="initUserList" @update_modelValue="handleUpdate"/>
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/dialog'

import { ref } from 'vue'
import {ElMessage} from "element-plus";

const tableData=ref([]);

const total=ref(0)

const dialogVisible=ref(false)

const dialogTitle=ref("")

const id=ref(-1)

const no=ref("")

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10
})
const initUserList=async()=>{
    const res=await requestUtil.post("info/teacher/list",queryForm.value);
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


const handleDialogValue=(sno)=>{
    if(sno)
    {
        //console.log(sno)
        dialogTitle.value="用户修改"
        dialogVisible.value=true
        no.value = sno;
        id.value=1;
    }else {
        dialogTitle.value = "用户添加"
        id.value=-2;
        no.value ="";
        dialogVisible.value = true
    }
}

const handleUpdate=()=>{
    dialogVisible.value=false
}

const handleDelete=async (sno)=>{
    //console.log("sssssssno"+sno)
    const res=await requestUtil.post("info/teacher/delete",sno)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '执行成功!'
        })
        await initUserList();
    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
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