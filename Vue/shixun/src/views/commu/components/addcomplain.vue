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
            <el-form-item label="内容" prop="content">
                <el-input type="textarea" v-model="form.content"/>
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
        }
    }
)
const form=ref({
    content:"",
    sno:"",
    cno:""
})

const rules=ref({
    content:[
        { required: true, message: '请输入内容'},
    ]
})

const formRef=ref(null)




watch(
    ()=>props.dialogVisible,
    ()=>{
            form.value={
                content:"",
                sender:"",
                cno:""
            }
    }
)






const emits=defineEmits(['update_modelValue','initUserList'])
const handleClose=()=>{
    emits('update_modelValue')


    console.log(props.dialogVisible)
}

const qcno = ref(sessionStorage.getItem("cno"))

const handleConfirm=()=>{
    formRef.value.validate(async(valid)=>{
        if(valid){
            form.value.sno = JSON.parse(sessionStorage.getItem("userInfo")).no;
            let cno = qcno.value;
            form.value.cno = cno;
            let result=await requestUtil.post("commu/complain/commit",form.value);
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