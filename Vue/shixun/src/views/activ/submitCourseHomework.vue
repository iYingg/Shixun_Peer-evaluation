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
            <div class="divider">
                <el-divider>
                    <el-icon><star-filled /></el-icon>
                </el-divider>
            </div>
        </div>
        <el-form
            ref="formRef"
            :rules="rules"
            label-width="100px"
        >
            <el-form-item label="" label-width="10px" label-position="top" prop="answer">
                <textarea
                        class="answer-textarea"
                        v-model="answer"
                        placeholder="在此输入您的答案"></textarea>
            </el-form-item>
        </el-form>

        <el-upload
                ref="upload"
                class="upload-demo"
                multiple="true"
                action=""
                :limit="1"
                :on-exceed="handleExceed"
                :auto-upload="false"
                :on-change="handleFileListChanged"
        >
            <template #trigger>
                <el-button type="success">选择附件</el-button>
            </template>
            <template #tip>
                <div class="el-upload__tip text-red">
                    limit 1 file, new file will cover the old file
                </div>
            </template>
        </el-upload>
        <el-button class="submit-btn" @click="submit">提交作业</el-button>
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

const answer = ref('');

const upload = ref(null)
const file = ref(null)

const form = ref({
    answer:'',
    sno:'',
    hid:0
})
const formRef = ref(null)

const rules=ref(
    {
        answer: [{ required: true, message: "请输入答案", trigger: "blur" }],
    }
)

/**
 * 处理上传文档超出范围
 * @param files
 */
const handleExceed = (files) => {
    upload.value.clearFiles()
    file.value = files[0]
    file.value.uid = genFileId()
    upload.value.handleStart(file)
}

const handleFileListChanged = (selectedFile)=>{
    file.value = selectedFile;
    //console.log(selectedFile.name);
    //console.log(file.value);

}


const editableTabsValue = ref(store.state.editableTabsValue)
const editableTabs = ref(store.state.editableTabs)

const submit=async ()=> {

    // formRef.value.validate(async(valid)=>{
    //     if(valid){
            form.value.sno = JSON.parse(sessionStorage.getItem("userInfo")).no;
            form.value.answer = answer.value;
            form.value.hid = parseInt(sessionStorage.getItem("hid"));
            let formData = new FormData();
            if (file.value !=null) {
                formData.append('multipartFile', file.value.raw);
            }
            formData.append('sno',form.value.sno);
            formData.append('answer',form.value.answer);
            formData.append('hid',form.value.hid);
            console.log(formData)
            let result=await requestUtil.fileUpload("activ/commithomework/commit",formData);
            let data=result.data;
            if(data.code==200){
                ElMessage.success("提交成功!")
                const tabs = editableTabs.value
                let activeName = editableTabsValue.value
                let targetName = `/home/activ/submitCourse${cno}${hid}`;
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