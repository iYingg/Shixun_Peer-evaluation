<template>
    <div class="app-container">
        <h1 class="title">{{qcname}}&nbsp;作业列表: </h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入作业标题..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
            <el-button type="success"  @click="handleDialogValue()">布置新作业</el-button>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%" tooltip-effect="light">
            <el-table-column prop="no" label="序号" width="70" />
            <el-table-column prop="htitle" label="作业标题" width="200" show-overflow-tooltip/>
            <el-table-column prop="hcontent" label="作业内容" width="300" show-overflow-tooltip/>
            <el-table-column prop="filename" label="附件" width="300" show-overflow-tooltip>
                <template v-slot="scope2">
                    <el-text v-if="scope2.row.filename==null||scope2.row.filename==''">无附件</el-text>
                    <el-text v-else-if="scope2.row.filename!=null">{{ scope2.row.filename }}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="deadline2" label="Deadline" width="150" />

            <el-table-column prop="hid" label="操作" width="300" >
                <template v-slot="scope" >
                    <el-tooltip
                        class="box-item"
                        effect="dark"
                        :content="scope.row.filename==null||scope.row.filename==''?'无附件':scope.row.filename"
                        placement="bottom"
                    >
                    <el-button
                        type="primary"
                        @click="download(scope.row.hid,scope.row.filename)"
                        :disabled="scope.row.filename==null||scope.row.filename==''?true:false"
                    >下载附件</el-button>
                    </el-tooltip>
                    <el-button
                        type="warning"
                        @click="handleDialogValue(scope.row.hid)"
                    >修改</el-button>
                    <el-popconfirm title="您确定要删除这条记录吗？" @confirm="handleDelete(scope.row.hid)">
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
    <Dialog :modelValue="dialogVisible" :dialogVisible="dialogVisible" :cno="qcno" :hid="send" :id="id"
            :dialogTitle="dialogTitle" @initUserList="initUserList" @update_modelValue="handleUpdate"/>
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/addhomeworkdialog.vue'


import { ref } from 'vue'
import {ElMessage} from "element-plus";
import {useRoute} from "vue-router";
import router from "@/router";

const tableData=ref([]);

const total=ref(0)

const dialogVisible=ref(false)

const dialogTitle=ref("")





const route = useRoute()


const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:'',
    cno:''
})



const qcno = ref(sessionStorage.getItem("cno"))
const qcname = ref(sessionStorage.getItem("cname"))




/**
 * 初始化列表
 * @returns {Promise<void>}
 */
const initUserList=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;


    queryForm.value.cno = cno;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/publishhomework/list",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkpublishList;
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

const id=ref(0);
const send=ref(0);

const handleDialogValue=(hid)=>{
    if(hid){
        dialogTitle.value = "修改作业"
        dialogVisible.value = true
        id.value = 1;
        send.value = hid;
    }else {
        dialogTitle.value = "发布作业"
        dialogVisible.value = true
        id.value = 0;
        send.value = 0;
    }
}

const handleUpdate=()=>{
    dialogVisible.value=false
}


const download=async (hid,fileName)=>{
    //console.log(hid);
    //console.log(fileName);
    let res = await requestUtil.Download("/activ/publishhomework/downhomework",hid,fileName)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '下载成功!'
        })
        await initUserList();
    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
}
const handleDelete=async (hid)=>{
    const res=await requestUtil.post("activ/publishhomework/delete",hid)
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