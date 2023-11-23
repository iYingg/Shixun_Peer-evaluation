<template>
    <el-dialog
        :modelValue="dialogVisible"
            :title="dialogTitle"
            width="30%"
            @close="handleClose"
    >
        <el-form
                ref="formRef"
                :model="form"
                :rules="rules"
                label-width="100px"
        >
            <el-form-item label="作业标题" prop="htitle">
                <el-input v-model="form.htitle"/>
            </el-form-item>
            <el-form-item label="作业内容" prop="hcontent">
                <el-input v-model="form.hcontent" type="textarea"/>
            </el-form-item>
            <el-form-item label="截止时间" prop="deadline2">
                <el-date-picker
                    v-model="form.deadline2"
                    type="date"
                    value-format="YYYY-MM-DD"
                    format="YYYY-MM-DD"
                    placeholder="Pick a date"
                    clearable
                />
            </el-form-item>
            <el-form-item label="附件" prop="annex">
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
                        <el-button type="primary">选择附件</el-button>
                    </template>
                    <template #tip>
                        <div class="el-upload__tip text-red">
                            limit 1 file, new file will cover the old file
                        </div>
                    </template>
                </el-upload>
            </el-form-item>
            <el-form-item label="互评参考答案" prop="annex">
                <el-upload
                    ref="upload2"
                    class="upload-demo2"
                    multiple="true"
                    action=""
                    :limit="1"
                    :on-exceed="handleExceed2"
                    :auto-upload="false"
                    :on-change="handleFileListChanged2"
                >
                    <template #trigger>
                        <el-button type="primary">选择附件</el-button>
                    </template>
                    <template #tip>
                        <div class="el-upload__tip text-red">
                            limit 1 file, new file will cover the old file
                        </div>
                    </template>
                </el-upload>
            </el-form-item>
        </el-form>
        <template #footer>

            <span class="dialog-footer">
            <el-button type="primary" @click="handleConfirm">确认</el-button>
            <el-button @click="handleClose">取消</el-button>
            </span>
        </template>
    </el-dialog>
</template>


<script setup>
import {defineEmits, defineProps,ref,watch } from "vue"
import requestUtil,{getServerUrl} from "@/util/request";
import { ElMessage } from 'element-plus'

import { genFileId } from 'element-plus'
import formatDate from "@/util/formatDate";

const upload = ref(null)
const file = ref(null)
const upload2 = ref(null)
const file2 = ref(null)

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

const handleExceed2 = (files) => {
    upload2.value.clearFiles()
    file2.value = files[0]
    file2.value.uid = genFileId()
    upload2.value.handleStart(file2)
}

const handleFileListChanged = (selectedFile)=>{
    file.value = selectedFile;
    //console.log(selectedFile.name);
    //console.log(file.value);

}

const handleFileListChanged2 = (selectedFile)=>{
    file2.value = selectedFile;
    //console.log(selectedFile.name);
    //console.log(file.value);

}

const props=defineProps(
    {
        dialogTitle:{
            type:String,
            default:'',
            required:true
        },
        dialogVisible:{
            type:Boolean,
            default:false,
            required:true
        },
        cno:{
            type:String,
            default:'',
            required:true
        },
        id:{
            type:Number,
            default:0,
            required:true
        },
        hid:{
            type:Number,
            default:0,
            required:true
        }
    }
)



const form=ref({
    htitle:'',
    hcontent:'',
    deadline2:null,
    cno:'',
    publisher:''
})






const rules=ref({
    htitle:[
        { required: true, message: '请输入作业标题'}
    ],
    hcontent: [{ required: true, message: "请输入作业内容", trigger: "blur" }],
    deadline: [{ required: true, message: "请选择截止时间", trigger: "blur" }]
})

const formRef=ref(null)




watch(
    ()=>props.dialogVisible,
    ()=>{
        let hid=props.hid;
        let id=props.id;
        if(id==1){
            initFormData(hid);
            if(upload.value!=null) {
                upload.value.clearFiles()
                file.value = null;
                upload2.value.clearFiles()
                file2.value = null;
            }
        }else{
                form.value={
                    htitle:'',
                    hcontent:'',
                    deadline2:null,
                    cno:'',
                    publisher:''
                }
            if(upload.value!=null) {
                upload.value.clearFiles();
                file.value=null;
                upload2.value.clearFiles()
                file2.value = null;
            }

        }
    }
)



const initFormData=async(hid)=>{
    const res=await requestUtil.get("activ/publishhomework/"+hid);
    console.log(res.data);
    form.value=res.data.homeworkpublish;
}


const emits=defineEmits(['update_modelValue','initUserList'])
const handleClose=()=>{
    console.log(upload);
    emits('update_modelValue')


    //console.log(props.dialogVisible)
}


const handleConfirm=()=>{
    formRef.value.validate(async(valid)=>{
        if(valid){

            form.value.cno = props.cno;
            form.value.publisher = JSON.parse(sessionStorage.getItem("userInfo")).no;
            let formData = new FormData();
            //console.log(file.value)
            //console.log(file.value!=null)
            if (file.value !=null) {
                //console.log("111111111111111111111111111111111111111111111")
                formData.append('multipartFile', file.value.raw);
            }
            formData.append('deadline2',form.value.deadline2);
            formData.append('cno',form.value.cno);
            formData.append('publisher',form.value.publisher);
            formData.append('hcontent',form.value.hcontent);
            formData.append('htitle',form.value.htitle);
            formData.append('hid',props.hid);
            formData.append('multipartFile2', file2.value.raw);
            //console.log(formData)
            let result=await requestUtil.fileUpload("activ/publishhomework/publish",formData);
            let data=result.data;
            if(data.code==200){
                ElMessage.success("执行成功！")
                formRef.value.resetFields();
                emits("initUserList")
                handleClose();
            }else{
                ElMessage.error(data.msg);
            }
        }else{
            console.log("fail")
        }
    })
}
</script>


<style lang="scss" scoped>
</style>