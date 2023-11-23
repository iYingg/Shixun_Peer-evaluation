<template>
    <div class="app-container">
        <h1 class="title">{{qcname}}:&nbsp;已提交作业列表: </h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入作业标题..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>

        </el-row>
        <el-table :data="tableData" stripe style="width: 100%" tooltip-effect="light">
            <el-table-column prop="no" label="序号" width="70" />
            <el-table-column prop="htitle" label="作业标题" width="170" show-overflow-tooltip/>

            <el-table-column prop="answer" label="您的答案" width="230" show-overflow-tooltip/>
            <el-table-column prop="filename2" label="附件名" width="200" show-overflow-tooltip>
                <template v-slot="scope2">
                    <el-text v-if="scope2.row.filename2==null||scope2.row.filename2==''">无附件</el-text>
                    <el-text v-else-if="scope2.row.filename2!=null">{{ scope2.row.filename2 }}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="commitdate" label="提交日期" width="120" />
            <el-table-column prop="deadline2" label="Deadline" width="120" />
            <el-table-column  label="互评情况" width="200" >
                <template v-slot="scope1">
                    <el-text v-if="scope1.row.unrevise!=0">有&nbsp;{{scope1.row.unrevise}}&nbsp;份作业需批改&nbsp;&nbsp;{{scope1.row.revisestatus}}</el-text>
                    <el-text v-else>已全部批改！&nbsp;&nbsp;{{scope1.row.revisestatus}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="hid" label="操作" width="200" >
                <template v-slot="scope" >
                    <el-tooltip
                            class="box-item"
                            effect="dark"
                            :content="scope.row.filename2==null||scope.row.filename2==''?'无附件':scope.row.filename2"
                            placement="bottom"
                    >
                        <el-button
                                type="primary"
                                @click="download(scope.row.hid,scope.row.filename2)"
                                :disabled="scope.row.filename2==null||scope.row.filename2==''?true:false"
                        >下载附件</el-button>
                    </el-tooltip>

                    <el-button
                        type="success"
                        @click="revisehomework(scope.row.hid,scope.row.htitle,scope.row.hcontent,scope.row.filename,scope.row.deadline2)"
                    >进入</el-button>

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
    <el-text type="danger" style="font-size: large;padding-left: 20px;padding-top: 100px">请在作业提交截止日期后一周内完成互批！</el-text>

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
    //console.log(cno);
    queryForm.value.cno = cno;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/commithomework/list",queryForm.value);
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
const send=ref({
    hid:0,
    sno:''
});


const revisehomework=(hid,htitle,hcontent,fileName,deadline2)=>{
    sessionStorage.setItem("hid",hid);
    sessionStorage.setItem("htitle",htitle);
    sessionStorage.setItem("hcontent",hcontent)
    sessionStorage.setItem("fileName",fileName)
    sessionStorage.setItem("deadline2",deadline2)
    let cname = sessionStorage.getItem("cname")
    let cno = sessionStorage.getItem("cno")
    let route = {
        name:'互评作业:'+ cname +':'+ hid,
        path:`/home/activ/gradeCourse${cno}${hid}`,
        meta:{
            parentName:'互评作业:'+cname
        },
    }

    route.component = () => import('@/views/activ/gradeCourseHomework.vue');

    router.addRoute('首页',route);


    router.push({
        path:`/home/activ/gradeCourse${cno}${hid}`,
    });
}





const download=async (hid,fileName)=>{
    //console.log(hid);
    //console.log(fileName);
    let res = await requestUtil.Download2("/activ/commithomework/downhomework",hid,JSON.parse(sessionStorage.getItem("userInfo")).no,fileName)
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