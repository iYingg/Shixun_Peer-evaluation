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
            <el-form-item label="课程号" prop="cno">
                <el-input v-model="form.cno"/>
            </el-form-item>
            <el-form-item label="课程名" prop="cname">
                <el-input v-model="form.cname" />
            </el-form-item>
            <el-form-item label="学时" prop="hours">
                <el-input v-model="form.hours" />
            </el-form-item>
            <el-form-item label="老师工号" prop="tno">
                <el-input v-model="form.tno" />
            </el-form-item>
            <el-form-item label="老师姓名" prop="tname"  >
                <el-input v-model="form.tname" disabled/>
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
    cno:"",
    cname:"",
    hours:"",
    tno:"",
    tname:"",
})

/**
 * 检查添加cno是否重复
 * @param rule
 * @param value
 * @param callback
 * @returns {Promise<void>}
 */
const checkNo = async (rule, value, callback) => {

        const res=await requestUtil.post("activ/courseAdm/checkNo",
            {cno:form.value.cno});
        if (res.data.code==500) {
            callback(new Error("课程号已存在！"));
        } else {
            callback();
        }

}


/**
 * 检查是否存在Tno
 * @param rule
 * @param value
 * @param callback
 * @returns {Promise<void>}
 */
const checkTno = async (rule, value, callback) => {
        const res=await requestUtil.post("info/user/checkTno",
            {no:form.value.tno});
        if (res.data.code==500) {
            callback(new Error("教师工号不存在！"));
        } else {
            const res2 =await requestUtil.get("info/user/"+form.value.tno);
            if (res2.data.code==500) {

                callback();
            } else {
                if(res2.data.peerUser.role == 3)
                    form.value.tname = res2.data.peerUser.name;
                callback();
            }

        }
}


const getTname = async (rule, value, callback) => {
    const res=await requestUtil.get("info/user/"+form.value.tno);
    if (res.data.code==500) {

        callback();
    } else {
        if(res.data.peerUser.role == 3)
            form.value.tname = res.data.peerUser.name;
        callback();
    }
}

const rules=ref({
    cno:[
        { required: true, message: '请输入课程号'},
        { required: true, validator: checkNo, trigger: "blur" }
    ],
    cname: [{ required: true, message: "课程名不能为空", trigger: "blur" }],
    hours: [{ required: true, message: "课时不能为空", trigger: "blur" }],
    tno: [{ required: true, message: "授课教师号不能为空", trigger: "blur" },
        { required: true, validator:checkTno, trigger: "blur" }],
    tname:[{required: true, validator:getTname, trigger: "blur"}]
})

const formRef=ref(null)




watch(
    ()=>props.dialogVisible,
    ()=>{
            form.value={
                cno:"",
                cname:"",
                hours:"",
                tno:"",
                tname:"",
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
            let result=await requestUtil.post("activ/courseAdm/addCourse",form.value);
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