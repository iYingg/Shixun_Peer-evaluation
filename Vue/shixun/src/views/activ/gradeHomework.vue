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
                <el-divider content-position="left">学生答案</el-divider>
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
        <el-form
            ref="formRef"
            :model="form2"
            :rules="rules"
            label-width="70px"
            class="demo-ruleForm"
            status-icon
        >
            <el-form-item label="评语：" prop="remark">
                <el-input v-model="form2.remark" type="textarea" placeholder="在此输入评语"/>
            </el-form-item>
            <el-form-item label="打分：" prop="score">
                <el-input v-model="form2.score" />
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

const answer2 = ref('22')

const file = ref(null)

const form = ref({
    answer:'',
    sno:'',
    hid:0
})
const formRef = ref(null)



const rules=ref(
    {
        remark: [{ required: true, message: "请输入评语", trigger: "blur" }],
        score:[{required: true, message: "请输入得分", trigger: "blur"},
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

const form2=ref({
    remark:'',
    score:0
})


const send=ref({
    hid:0,
    commitsno:''
})

const fileName2 = ref('')
const getAnswer = async ()=>{
    send.value.hid = parseInt(hid);
    send.value.commitsno = sessionStorage.getItem("commitsno")
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






const editableTabsValue = ref(store.state.editableTabsValue)
const editableTabs = ref(store.state.editableTabs)

const submit=async ()=> {

    // formRef.value.validate(async(valid)=>{
    //     if(valid){
            let formData = new FormData();
            formData.append('reviser',JSON.parse(sessionStorage.getItem("userInfo")).no);
            formData.append('commitsno',sessionStorage.getItem("commitsno"));
            formData.append('hid',parseInt(sessionStorage.getItem("hid")));
            formData.append('score',form2.value.score);
            formData.append('remark',form2.value.remark);
            //console.log(formData)
            let result=await requestUtil.fileUpload("activ/revisehomework/revise",formData);
            let data=result.data;
            if(data.code==200){
                ElMessage.success("提交成功!")
                const tabs = editableTabs.value
                let activeName = editableTabsValue.value
                let targetName = `/home/activ/gradeCourse${cno}${hid}/grade`;
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


const download=async ()=>{
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

const download2=async (hid,fileName)=>{
    //console.log(hid);
    //console.log(fileName);
    let res = await requestUtil.Download2("/activ/commithomework/downhomework",hid,sessionStorage.getItem("commitsno"),fileName)
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

.submit-btn:hover {
    background-color: darkblue;
}
</style>