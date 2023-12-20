<template>
    <div class="app-container">
        <h1 class="title">欢迎来到讨论区:</h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入发帖内容..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
            <el-button type="success"  @click="handleDialogValue()">发帖
            </el-button>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="content" label="发帖内容" width="700" />
            <el-table-column prop="sender" label="发帖人" width="250" />
            <el-table-column prop="date2" label="发帖时间" width="300" >
                <template v-slot="scope">
                    <el-text>{{scope.row.date2}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-text>

                    <el-icon size="18" v-if="scope.row.sender === userno" @click="deletechat(scope.row.chatid)"><CircleClose /></el-icon>
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
    <Dialog :modelValue="dialogVisible" :dialogVisible="dialogVisible"
            :dialogTitle="dialogTitle" @initUserList="initUserList" @update_modelValue="handleUpdate"/>
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/addchat'
import { ref } from 'vue'
import {ElMessage} from "element-plus";

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
    const res=await requestUtil.post("commu/chat/list",queryForm.value);
    tableData.value=res.data.chatList;
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

const dialogVisible=ref(false)

const dialogTitle=ref("")


const handleDialogValue=()=>{
    dialogTitle.value = "发帖"
    dialogVisible.value = true
}

const handleUpdate=()=>{
    dialogVisible.value=false
}

const userno = ref(JSON.parse(sessionStorage.getItem("userInfo")).no)

const send = ref({
    chatid:0,

})

const deletechat=async(chatid)=>{
    send.value.chatid = chatid;

    const res=await requestUtil.post("commu/chat/delete",send.value);
    if(res.data.code==200){
        await initUserList();
        ElMessage.success("删除成功");
    }
    else{
        ElMessage.error("删除失败");
    }
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