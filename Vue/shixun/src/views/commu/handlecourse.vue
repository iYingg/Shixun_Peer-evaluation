<template>
    <div class="app-container">
        <h1 class="title">申诉列表:</h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入课程名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initselect">搜索</el-button>
            <el-col :span="15" style="display: flex;justify-content: flex-end">

                <el-radio-group v-model="radio1" size="large">
                    <el-radio-button label="未回复" />
                    <el-radio-button label="已回复" />
                </el-radio-group>

            </el-col>
        </el-row>

        <el-table :data="tableData" v-if="radio1==='未回复'" stripe style="width: 100%">
            <el-table-column prop="complainid" label="申诉ID" width="150" />
            <el-table-column prop="commitcontent" label="申诉内容" width="400" />
            <el-table-column prop="commitdate" label="提交时间" width="300" />
            <el-table-column prop="tname" label="提交学生" width="250" />
            <el-table-column prop="isChosen" label="操作" width="200" >
                <template v-slot="scope" >
                    <el-button type="success" @click="handleDialogValue(scope.row.complainid)">回复</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-table :data="tableData" v-if="radio1==='已回复'" stripe style="width: 100%">
            <el-table-column prop="complainid" label="申诉ID" width="100" />
            <el-table-column prop="commitcontent" label="申诉内容" width="270" />
            <el-table-column prop="respondcontent" label="回复内容" width="270" />
            <el-table-column prop="commitdate" label="提问时间" width="150" />
            <el-table-column prop="responddate" label="回复时间" width="150" />
            <el-table-column prop="tname" label="提交学生" width="150" />
            <el-table-column prop="isChosen" label="操作" width="200" >
                <template v-slot="scope" >
                    <el-popconfirm title="您确定要撤销这个回复吗？" @confirm="handleDelete(scope.row.complainid)">
                        <template #reference>
                            <el-button type="danger" >撤销</el-button>
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


import {ref, watch} from 'vue'
import {ElMessage} from "element-plus";
import Dialog from "@/views/commu/components/addhandle";

const tableData=ref([[]]);


const total=ref(0)


const radio1 = ref('未回复')

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:'',
    cno:''
})

const post=ref({
    complainid:''
})

/**
 * 选择
 * @returns {Promise<void>}
 */
const initselect= async ()=>{
    if(radio1.value ==='已回复'){
        await initUnchosen();
    }
    else
        await initUserList();
}

const qcno = ref(sessionStorage.getItem("cno"))

/**
 * 初始化列表
 * @returns {Promise<void>}
 */
const initUserList=async()=>{
    let cno = qcno.value;
    queryForm.value.cno = cno;
    const res=await requestUtil.post("commu/handle/listunrespond",queryForm.value);
    tableData.value=res.data.complainlist;
    total.value=res.data.total;
}


/**
 * 未选的课表
 * @returns {Promise<void>}
 */
const initUnchosen=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    queryForm.value.cno = cno;
    const res=await requestUtil.post("commu/handle/listrespond",queryForm.value);
    tableData.value=res.data.complainlist;
    total.value=res.data.total;
}

initUserList();


/**
 * 退出课程
 * @param cno
 * @returns {Promise<void>}
 */
const handleDelete=async (complainid)=>{
    post.value.complainid = complainid
    const res=await requestUtil.post("commu/handle/delete",post.value)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '删除成功!'
        })
        radio1.value = '未回复';
    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
}

const handleSizeChange=(pageSize)=>{
    queryForm.value.pageNum=1;
    queryForm.value.pageSize=pageSize;
    initUserList()
}

const handleCurrentChange=(pageNum)=>{
    queryForm.value.pageNum=pageNum;
    initUserList()
}



watch(
    ()=>radio1.value,
    ()=>{
        if(radio1.value =='未回复'){
            initUserList();
        }else if(radio1.value == '已回复'){
            initUnchosen();
        }

    }
)


const dialogVisible=ref(false)

const dialogTitle=ref("")


const handleDialogValue=(complainid)=>{
    sessionStorage.setItem("complainid",complainid)
    dialogTitle.value = "回复"
    dialogVisible.value = true
}

const handleUpdate=()=>{
    dialogVisible.value=false
}

</script>


<style lang="scss" scoped>
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

.title{
    font-size: x-large;
    font-weight: bold;
}
</style>