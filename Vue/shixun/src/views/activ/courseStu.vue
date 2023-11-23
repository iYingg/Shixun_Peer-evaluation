<template>
    <div class="app-container">
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入课程名..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initselect">搜索</el-button>
            <el-col :span="15" style="display: flex;justify-content: flex-end">

                    <el-radio-group v-model="radio1" size="large">
                        <el-radio-button label="已选" />
                        <el-radio-button label="未选" />
                        <el-radio-button label="全部" />
                    </el-radio-group>

            </el-col>
        </el-row>

        <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="cno" label="课程号" width="250" />
            <el-table-column prop="cname" label="课程名" width="250" />
            <el-table-column prop="hours" label="课时" width="300" />
            <el-table-column prop="tname" label="授课老师" width="300" />
            <el-table-column prop="isChosen" label="操作" width="180" >
                <template v-slot="scope" >
                    <el-button type="success" v-if=" scope.row.isChosen == 0" @click="joinCourse(scope.row.cno)">加入课程</el-button>
                    <el-popconfirm title="您确定要退出这门课吗？" @confirm="handleDelete(scope.row.cno)" v-if="scope.row.isChosen == 1">
                        <template #reference>
                            <el-button type="danger" >退出课程</el-button>
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
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/addcoursedialog'


import {ref, watch} from 'vue'
import {ElMessage} from "element-plus";

const tableData=ref([[]]);


const total=ref(0)


const radio1 = ref('已选')

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:''
})

const post=ref({
    userNo:'',
    cno:''
})

/**
 * 选择
 * @returns {Promise<void>}
 */
const initselect= async ()=>{
    if(radio1.value =='全部'){
        initAll();
    }
    else
        radio1.value = '全部';
}

/**
 * 初始化列表
 * @returns {Promise<void>}
 */
const initUserList=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/courseStu/listChosen",queryForm.value);
    tableData.value=res.data.courseList;
    total.value=res.data.total;
}


/**
 * 未选的课表
 * @returns {Promise<void>}
 */
const initUnchosen=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/courseStu/listUnchosen",queryForm.value);
    tableData.value=res.data.courseList;
    total.value=res.data.total;
}

/**
 * 全部的课表
 * @returns {Promise<void>}
 */
const initAll=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/courseStu/listAll",queryForm.value);
    tableData.value=res.data.courseList;
    total.value=res.data.total;
}

initUserList();

/**
 * 加入课程
 * @param cno
 * @returns {Promise<void>}
 */
const joinCourse=async (cno)=>{
    post.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    post.value.cno = cno;
    const res=await requestUtil.post("activ/courseStu/joinCourse",post.value)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '加入成功!'
        })
        if(radio1.value =='已选'){
            await initUserList();
        }
        else
            radio1.value = '已选';
    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
}

/**
 * 退出课程
 * @param cno
 * @returns {Promise<void>}
 */
const handleDelete=async (cno)=>{
    post.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    post.value.cno = cno;
    const res=await requestUtil.post("activ/courseStu/dropCourse",post.value)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '退出成功!'
        })
        if(radio1.value =='已选'){
            await initUserList();
        }
        else
            radio1.value = '已选';
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
        if(radio1.value =='已选'){
            initUserList();
        }else if(radio1.value == '未选'){
            initUnchosen();
        }else{
            initAll();
        }

    }
)
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