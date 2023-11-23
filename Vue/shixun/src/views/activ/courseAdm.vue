<template>
    <div class="app-container">
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入课程名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
            <el-button type="success"  @click="handleDialogValue()">新增</el-button>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="cno" label="课程号" width="250" />
            <el-table-column prop="cname" label="课程名" width="250" />
            <el-table-column prop="hours" label="课时" width="300" />
            <el-table-column prop="tname" label="授课老师" width="300" />
            <el-table-column prop="action" label="操作" width="180" >
                <template v-slot="scope" >
<!--                    <el-button type="primary" @click="handleDialogValue(scope.row.cno)" >修改</el-button>-->
                    <el-popconfirm title="您确定要删除这条记录吗？" @confirm="handleDelete(scope.row.cno)">
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
    <Dialog :modelValue="dialogVisible" :dialogVisible="dialogVisible"
            :dialogTitle="dialogTitle" @initUserList="initUserList" @update_modelValue="handleUpdate"/>
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/addcoursedialog'


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


/**
 * 初始化列表
 * @returns {Promise<void>}
 */

const initUserList=async()=>{
    const res=await requestUtil.post("activ/courseAdm/list",queryForm.value);
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


const handleDialogValue=()=>{
        dialogTitle.value = "添加课程"
        dialogVisible.value = true
}

const handleUpdate=()=>{
    dialogVisible.value=false
}

const handleDelete=async (cno)=>{
    const res=await requestUtil.post("activ/courseAdm/delete",cno)
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