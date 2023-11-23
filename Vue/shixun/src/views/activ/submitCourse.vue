<template>
    <div class="app-container">
        <h1 class="title">{{qcname}}&nbsp;作业列表: </h1>
        <el-row :gutter="20" class="header">
            <el-col :span="7">
                <el-input placeholder="请输入作业标题..." v-model="queryForm.query" clearable
                ></el-input>
            </el-col>
            <el-button type="primary"  @click="initUserList">搜索</el-button>
            <el-col :span="15" style="display: flex;justify-content: flex-end">

                <el-radio-group v-model="radio1" size="large">
                    <el-radio-button label="未提交" />
                    <el-radio-button label="已提交" />
                    <el-radio-button label="全部" />
                </el-radio-group>

            </el-col>
        </el-row>

        <el-text class="h2" v-if="radio1=='未提交'" style="display: flex;justify-content: flex-end;padding-right: 50px">有{{total}}份未交作业 </el-text>
        <el-text class="h3" v-else-if="radio1=='已提交'" style="display: flex;justify-content: flex-end;padding-right: 50px" >已经提交{{total}}份作业</el-text>
        <el-table class="table" :data="tableData" stripe style="width: 100%" tooltip-effect="light" v-if="radio1!='已提交'">
            <el-table-column prop="no" label="序号" width="250" />
            <el-table-column prop="htitle" label="作业标题" width="250" show-overflow-tooltip/>
            <el-table-column prop="hcontent" label="作业内容" width="300" show-overflow-tooltip/>
            <el-table-column prop="deadline2" label="Deadline" width="300" />
            <el-table-column prop="hid" label="操作" width="200" >
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
                    <el-popconfirm
                        title="您确定要删除这条记录吗？"
                        @confirm="handleDelete(scope.row.hid)"
                        v-if="scope.row.isCommit=='Y'"
                    >
                        <template #reference>
                            <el-button type="danger" >撤回</el-button>
                        </template>
                    </el-popconfirm>
                    <el-button
                        type="success"
                        v-if="scope.row.isCommit=='N'"
                        @click="submithomework(scope.row.hid,scope.row.htitle,scope.row.hcontent,scope.row.filename,scope.row.deadline2)"
                    >提交</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-table :data="tableData" stripe style="width: 100%;padding-top: 15px" tooltip-effect="light" v-else>
            <el-table-column prop="no" label="序号" width="70" />
            <el-table-column prop="htitle" label="作业标题" width="180" show-overflow-tooltip/>
            <el-table-column prop="hcontent" label="作业内容" width="180" show-overflow-tooltip/>
            <el-table-column prop="answer" label="您的答案" width="230" show-overflow-tooltip/>
            <el-table-column prop="filename2" label="附件名" width="200" show-overflow-tooltip>
                <template v-slot="scope2">
                    <el-text v-if="scope2.row.filename2==null||scope2.row.filename2==''">无附件</el-text>
                    <el-text v-else-if="scope2.row.filename2!=null">{{ scope2.row.filename2 }}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="commitdate" label="提交日期" width="120" />
            <el-table-column prop="deadline2" label="Deadline" width="120" />

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
                            @click="download2(scope.row.hid,scope.row.filename2)"
                            :disabled="scope.row.filename2==null||scope.row.filename2==''?true:false"
                        >下载附件</el-button>
                    </el-tooltip>

                    <el-popconfirm
                        title="您确定要删除这条记录吗？"
                        @confirm="handleDelete(scope.row.hid)"
                        v-if="scope.row.isCommit=='Y'"
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

const radio1 = ref('未提交')

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
 * 初始化列表未提交
 * @returns {Promise<void>}
 */
const initUserList=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/commithomework/unpublishlist",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkpublishList;
    total.value=res.data.total;
}

const initPublish=async()=>{
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

const initAll=async()=>{
    queryForm.value.userNo = JSON.parse(sessionStorage.getItem("userInfo")).no;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/commithomework/listAll",queryForm.value);
    //console.log(res.data.homeworkpublishList);
    tableData.value=res.data.homeworkpublishList;
    total.value=res.data.total;
}

initUserList();

watch(
    ()=>radio1.value,
    ()=>{
        if(radio1.value =='未提交'){
            initUserList();
        }else if(radio1.value == '已提交'){
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
    sno:''
});


const submithomework=(hid,htitle,hcontent,fileName,deadline2)=>{
    sessionStorage.setItem("hid",hid);
    sessionStorage.setItem("htitle",htitle);
    sessionStorage.setItem("hcontent",hcontent)
    sessionStorage.setItem("fileName",fileName)
    sessionStorage.setItem("deadline2",deadline2)
    let cname = sessionStorage.getItem("cname")
    let cno = sessionStorage.getItem("cno")
    let route = {
        name:'提交作业:'+ cname +':'+ hid,
        path:`/home/activ/submitCourse${cno}${hid}`,
        meta:{
            parentName:'提交作业:'+cname
        },
    }

    route.component = () => import('@/views/activ/submitCourseHomework.vue');

    router.addRoute('首页',route);


    router.push({
        path:`/home/activ/submitCourse${cno}${hid}`,
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
const handleDelete=async (hid)=>{
    send.value.hid = hid;
    send.value.sno = JSON.parse(sessionStorage.getItem("userInfo")).no;
    const res=await requestUtil.post("activ/commithomework/delete",send.value)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '执行成功!'
        })
        radio1.value='未提交';
    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
}


const download2=async (hid,fileName)=>{
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

.table{
    padding-top: 15px;
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