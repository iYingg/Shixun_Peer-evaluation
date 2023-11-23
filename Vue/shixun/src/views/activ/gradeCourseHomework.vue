<template>
    <div class="app-container">
        <h1 class="title">{{qcname}}:&nbsp;{{ qhtitle }}:&nbsp;互评作业 </h1>
        <el-row :gutter="20" class="header">
            <el-text class="h2" v-if="radio1=='未批改'" style="display: flex;padding-left: 20px">有{{total}}份未批作业 </el-text>
            <el-text class="h3" v-else-if="radio1=='已批改'" style="display: flex;padding-left: 20px" >已经批改{{total}}份作业</el-text>
            <el-text class="h3" v-else-if="radio1=='全部'" style="display: flex;padding-left: 20px" >共需批改{{total}}份作业</el-text>
            <el-col :span="5">

            </el-col>

            <el-col :span="16" style="display: flex;justify-content: flex-end">

                <el-radio-group v-model="radio1" size="large">
                    <el-radio-button label="未批改" />
                    <el-radio-button label="已批改" />
                    <el-radio-button label="全部" />
                </el-radio-group>

            </el-col>
        </el-row>


        <el-table
            class="table"
            :data="tableData" stripe
            style="width: 100%;padding-top: 15px"
            :header-cell-style=" {'text-align':'center'}"
            :cell-style=" {'text-align':'center'}"
            tooltip-effect="light" v-if="radio1=='未批改'">
            <el-table-column prop="no" label="序号" width="700" >
                <template v-slot="ss">
                    <el-text>Student&nbsp;{{ss.row.no}}</el-text>
                </template>
            </el-table-column>

            <el-table-column prop="hid" label="操作" width="700" >
                <template v-slot="scope" >

                    <el-button
                        type="success"
                        @click="gradehomework(scope.row.commitsno)"
                    >批改</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-table
            class="table"
            :data="tableData" stripe
            style="width: 100%;padding-top: 15px"
            :header-cell-style=" {'text-align':'center'}"
            :cell-style=" {'text-align':'center'}"
            tooltip-effect="light" v-if="radio1=='已批改'">
            <el-table-column prop="no" label="序号" width="200" >
                <template v-slot="ss">
                    <el-text>Student&nbsp;{{ss.row.no}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="评语"  width="500"/>
            <el-table-column prop="score" label="打分"  width="300"/>

            <el-table-column prop="hid" label="操作" width="300" >
                <template v-slot="scope" >
                    <el-popconfirm
                        title="您确定要删除这条记录吗？"
                        @confirm="handleDelete(scope.row.commitsno)"
                    >
                        <template #reference>
                            <el-button type="danger" >撤回</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-table
            class="table"
            :data="tableData" stripe
            style="width: 100%;padding-top: 15px"
            :header-cell-style=" {'text-align':'center'}"
            :cell-style=" {'text-align':'center'}"
            tooltip-effect="light" v-if="radio1=='全部'">
            <el-table-column prop="no" label="序号" width="200" >
                <template v-slot="ss">
                    <el-text>Student&nbsp;{{ss.row.no}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="评语"  width="500"/>
            <el-table-column prop="score" label="打分"  width="300"/>

            <el-table-column prop="hid" label="操作" width="300" >
                <template v-slot="scope" >
                    <el-button
                        type="success"
                        @click="gradehomework(scope.row.commitsno)"
                        v-if="scope.row.status=='N'"
                    >批改</el-button>
                    <el-popconfirm
                        title="您确定要删除这条记录吗？"
                        @confirm="handleDelete(scope.row.commitsno)"
                        v-if="scope.row.status=='Y'"
                    >
                        <template #reference>
                            <el-button type="danger" >撤回</el-button>
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
    <el-text type="danger" style="font-size: large;padding-left: 20px;padding-top: 100px">请在作业提交截止日期后一周内完成互评！</el-text>
</template>
<script setup>
import requestUtil,{getServerUrl} from "@/util/request";
import Dialog from './components/addhomeworkdialog.vue'


import {ref, watch} from 'vue'
import {ElMessage} from "element-plus";
import {useRoute} from "vue-router";
import router from "@/router";

const tableData=ref([]);

const total=ref(0)

const radio1 = ref('未批改')

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:'',
    cno:'',
    hid:''
})

const qcno = ref(sessionStorage.getItem("cno"))
const qcname = ref(sessionStorage.getItem("cname"))
const qhtitle = ref(sessionStorage.getItem("htitle"));
const qhid = ref(sessionStorage.getItem("hid"));
/**
 * 初始化列表未提交
 * @returns {Promise<void>}
 */
const initUserList=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    queryForm.value.hid = qhid.value;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/revisehomework/listdisrevise",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkreviseList;
    total.value=res.data.total;
}

const initPublish=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    queryForm.value.hid = qhid.value;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/revisehomework/listrevise",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkreviseList;
    total.value=res.data.total;
}

const initAll=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    queryForm.value.hid = qhid.value;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/revisehomework/listall",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkreviseList;
    total.value=res.data.total;
}

initUserList();

watch(
    ()=>radio1.value,
    ()=>{
        if(radio1.value =='未批改'){
            initUserList();
        }else if(radio1.value == '已批改'){
            initPublish();
        }else{
            initAll();
        }

    }
)
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
const send=ref({
    hid:0,
    reviser:'',
    commitsno:''
});


const gradehomework=(commitsno)=>{
    sessionStorage.setItem("commitsno",commitsno)

    let hid = sessionStorage.getItem("hid")
    let cname = sessionStorage.getItem("cname")
    let cno = sessionStorage.getItem("cno")
    let route = {
        name:'开始互评',
        path:`/home/activ/gradeCourse${cno}${hid}/grade`,
        meta:{
            parentName:'互评作业:'+ cname +':'+ hid
        },
    }

    route.component = () => import('@/views/activ/gradeHomework.vue');

    router.addRoute('首页',route);


    router.push({
        path:`/home/activ/gradeCourse${cno}${hid}/grade`,
    });
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
const handleDelete=async (commitsno)=>{
    send.value.hid = parseInt(qhid.value);
    send.value.reviser = JSON.parse(sessionStorage.getItem("userInfo")).no;
    send.value.commitsno = commitsno;
    const res=await requestUtil.post("activ/revisehomework/revoke",send.value)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '执行成功!'
        })
        radio1.value='未批改';
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

.h2{
    font-size: large;
    padding-top: 5px;
}
.h3{
    font-size: large;
    padding-top: 5px;
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