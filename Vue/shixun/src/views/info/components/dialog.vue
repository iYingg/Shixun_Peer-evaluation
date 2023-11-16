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
            <el-form-item label="学号/工号" prop="no">
                <el-input v-model="form.no" :disabled="form.id<0?false:'disabled'" />
                <el-alert
                        v-if="form.no==''"
                        title="默认初始密码：123456"
                        :closable="false"
                        style="line-height: 10px;"
                        type="success" >
                </el-alert>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="密码" prop="password"  v-if="form.id>0">
                <el-input v-model="form.password" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" />
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
        id:{
            type:Number,
            default:-1,
            required:true
        },
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
        no:{
            type:String,
            default:"",
            required:true
        }

    }
)
const form=ref({
    id:-1,
    no:"",
    name:"",
    password:"123456",
    phone:"",
    email:"",
    role:"2"

})

/**
 * 检查添加学号是否重复
 * @param rule
 * @param value
 * @param callback
 * @returns {Promise<void>}
 */
const checkNo = async (rule, value, callback) => {
    if(form.value.id==-1){
        const res=await requestUtil.post("info/user/checkNo",
            {no:form.value.no});
        if (res.data.code==500) {
            callback(new Error("学号/工号已存在！"));
        } else {
            callback();
        }
    }else{
        callback();
    }
}

const rules=ref({
    no:[
        { required: true, message: '请输入用户名'},
        { required: true, validator: checkNo, trigger: "blur" }
    ],
    name: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
    password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
    email: [{ required: true, message: "邮箱地址不能为空", trigger: "blur" }, { type:
            "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
    phone: [{ required: true, message: "手机号码不能为空", trigger: "blur" }, {
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger:
            "blur" }],
})

const formRef=ref(null)

/**
 *从后端根据学号获取数据来修改信息
 * @param no
 * @returns {Promise<void>}
 */
const initFormData=async(no)=>{
    const res=await requestUtil.get("info/user/"+no);
    console.log(res.data);
    form.value=res.data.peerUSer;
}



watch(
    ()=>props.dialogVisible,
    ()=>{
        let no=props.no;
        let id=props.id;
        console.log("no="+no)
        if(no!=""){
            initFormData(no);
        }else{
            if(id==-1)
            form.value={
                id:-1,
                no:"",
                name:"",
                password:"123456",
                phone:"",
                email:"",

            }
            else form.value={
                id:-2,
                no:"",
                name:"",
                password:"123456",
                phone:"",
                email:"",

            }
        }
    }
)

const emits=defineEmits(['update_modelValue','initUserList'])
const handleClose=()=>{
    emits('update_modelValue')


    console.log(props.dialogVisible)
}
const handleConfirm=()=>{
    formRef.value.validate(async(valid)=>{
        if(valid){
            let result=await requestUtil.post("info/student/save",form.value);
            if(form.value.id==-2)
                result=await requestUtil.post("info/teacher/save",form.value);
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