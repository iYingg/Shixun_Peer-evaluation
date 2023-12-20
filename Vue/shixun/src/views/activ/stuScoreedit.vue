<template>
    <div class="assignment-container">
        <div class="assignment-details">
            <h1 class="title">作业标题:{{htitle}}</h1>
            <p class="deadline">&nbsp;截止时间:&nbsp;{{deadline2}}</p>
            <p class="label">&nbsp;作业内容：</p>
            <p class="content">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ hcontent }}</p>
            <div class="button">
                <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="fileName==null||fileName==''?'无附件':fileName"
                    placement="bottom"
                >
                    <el-button
                            @click="download"
                            style="float: right"
                            :disabled="fileName==null||fileName==''?true:false"
                    >下载附件</el-button>
                </el-tooltip>
            </div>
            <div class="answer" style="padding-top: 50px">
                <el-divider content-position="left">您的答案</el-divider>
                <el-text style="font-size: medium;padding-right: 20px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{answer2}}</el-text>
            </div>
            <div class="button">
                <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="fileName2==null||fileName2==''?'无附件':fileName2"
                    placement="bottom"
                >
                    <el-button
                        @click="download2(hid,fileName2)"
                        style="float: right"
                        :disabled="fileName2==null||fileName2==''?true:false"
                    >下载附件</el-button>
                </el-tooltip>
            </div>
            <div class="divider">
                <el-divider>
                    <el-icon><star-filled /></el-icon>
                </el-divider>
            </div>
        </div>
        <el-table class="table" :data="tableData2" stripe style="width: 100%" tooltip-effect="light" >
            <el-table-column prop="no" label="序号" width="180" >
                <template v-slot="ss">
                    <el-text>Student&nbsp;{{ss.row.no}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="评语" width="300" show-overflow-tooltip/>
            <el-table-column prop="score" label="得分" width="150" show-overflow-tooltip/>
        </el-table>
        <el-text class="final" style="font-size: large;font-weight: bold"><br><br>原最终得分：{{finalscore}}</el-text>
        <el-form
            ref="formRef"
            :model="form2"
            :rules="rules"
            label-width="120px"
            class="demo-ruleForm"
            style="padding-top: 20px;font-size: xx-large;font-weight: bold"
            status-icon
        >
            <el-form-item label="修改最终得分：" prop="finalscore">
                <el-input v-model="form2.finalscore" />
            </el-form-item>


        </el-form>
        <el-button class="submit-btn" @click="submit">提交</el-button>
    </div>

</template>

<script setup>
import { ref } from 'vue';
import requestUtil from "@/util/request";
import {ElMessage, genFileId} from "element-plus";
import router from "@/router";
import store from "@/store";

let htitle = sessionStorage.getItem("htitle");
let hcontent = sessionStorage.getItem("hcontent")
let fileName = sessionStorage.getItem("fileName")
let hid = sessionStorage.getItem("hid");
let deadline2 = sessionStorage.getItem("deadline2")
let cno = sessionStorage.getItem("cno")
let sno = sessionStorage.getItem("sno")
let finalscore = sessionStorage.getItem("finalscore")

const answer2 = ref('22')

const tableData2=ref([]);

const queryForm=ref({
    query:'',
    pageNum:1,
    pageSize:10,
    userNo:'',
    cno:'',
    hid:''
})
const total=ref(0)
const qcno = ref(sessionStorage.getItem("cno"))
const qhid = ref(sessionStorage.getItem("hid"));
const initTable=async()=>{
    queryForm.value.userNo = sno;
    let cno = qcno.value;
    //console.log(cno);
    queryForm.value.cno = cno;
    queryForm.value.hid = qhid.value;
    //console.log(queryForm);
    const res=await requestUtil.post("activ/revisehomework/listremark",queryForm.value);
    tableData2.value=res.data.homeworkreviseList;
    total.value=res.data.total;
}
initTable();

const handleSizeChange=(pageSize)=>{
    queryForm.value.pageNum=1;
    queryForm.value.pageSize=pageSize;
    initTable();
}

const handleCurrentChange=(pageNum)=>{
    queryForm.value.pageNum=pageNum;
    initTable();
}

const send=ref({
    hid:0,
    commitsno:''
})

const fileName2 = ref('')
const getAnswer = async ()=>{
    send.value.hid = parseInt(hid);
    send.value.commitsno = sno;
    let result = await requestUtil.post("activ/revisehomework/getAnswer",send.value)
    let data=result.data;
    if(data.code==200){
        answer2.value = data.homeworkcommit.answer
        fileName2.value = data.homeworkcommit.filename

    }else{
        ElMessage.error(data.msg);
    }
}

getAnswer();




const download=async ()=>{
    let res = await requestUtil.Download("/activ/publishhomework/downhomework",hid,fileName)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '下载成功!'
        })
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
    let res = await requestUtil.Download2("/activ/commithomework/downhomework",hid,sessionStorage.getItem("commitsno"),fileName)
    if(res.data.code==200){
        ElMessage({
            type: 'success',
            message: '下载成功!'
        })

    }else{
        ElMessage({
            type: 'error',
            message: res.data.msg,
        })
    }
}


const form2=ref({
    finalscore:0
})

const rules=ref(
    {
        finalscore:[{required: true, message: "请输入得分", trigger: "blur"},
            { type:'number', message: "请输入正确数字", trigger: "blur"},
            {validator: (rule, value, callback) => {
                    const number = parseInt(value);
                    if (isNaN(number) || number < 1 || number > 100) {
                        callback(new Error('请输入1-100之间的数字'));
                    } else {
                        callback();
                    }
                },
                trigger: 'blur'
            }]
    }
)

const formRef = ref(null)


const editableTabsValue = ref(store.state.editableTabsValue)
const editableTabs = ref(store.state.editableTabs)
const submit=async ()=> {

    // formRef.value.validate(async(valid)=>{
    //     if(valid){
    let formData = new FormData();

    formData.append('sno',sessionStorage.getItem("sno"));
    formData.append('hid',parseInt(sessionStorage.getItem("hid")));
    formData.append('finalscore',form2.value.finalscore);

    //console.log(formData)
    let result=await requestUtil.fileUpload("activ/stu/edit",formData);
    let data=result.data;
    if(data.code==200){
        ElMessage.success("修改成功!")
        const tabs = editableTabs.value
        let activeName = editableTabsValue.value
        let targetName = `/home/activ/scoreCourse${cno}${hid}${sno}edit`;
        if (activeName === targetName) {
            tabs.forEach((tab, index) => {
                if (tab.name === targetName) {
                    const nextTab = tabs[index + 1] || tabs[index - 1]
                    if (nextTab) {
                        activeName = nextTab.name
                    }
                }
            })
        }
        editableTabsValue.value = activeName
        editableTabs.value = tabs.filter((tab) => tab.name !== targetName)

        store.state.editableTabsValue = editableTabsValue.value;
        store.state.editableTabs = editableTabs.value

        await router.push({path: activeName})

    }else{
        ElMessage.error(data.msg);
    }
    //     }else{
    //         console.log("fail")
    //     }
    // })
}

</script>

<style>
.title{
    font-size: x-large;
    font-weight: bold;
}

.content{

    font-size: medium;
}

.label{
    font-size: medium;

    padding-top: 5px;
}

.deadline{
    padding-top: 10px;
}
.divider{
    padding-top: 50px;
}

.button{
    padding-top: 20px;
}

.assignment-container {
    max-width: 600px;
    margin: auto;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.assignment-details h1 {
    margin-bottom: 10px;
}

.assignment-details p {
    margin-bottom: 20px;
}


.answer-textarea {
    width: 100%;
    height: 150px;
    margin-bottom: 20px;
    padding: 10px;
    font-size: 16px;
    border: 1px solid #ddd;
}

.file-upload input[type="file"] {
    margin-bottom: 20px;
}

.submit-btn {
    width: 100%;
    padding: 10px;
    background-color: blue;
    color: white;
    border: none;
    cursor: pointer;
}

.final{

}

.submit-btn {
    width: 100%;
    padding: 10px;
    background-color: blue;
    color: white;
    border: none;
    cursor: pointer;
}
</style>