<template>
<!--    <el-button type="primary" @click="handleLogin">测试登陆</el-button>-->
<!--    <el-button type="danger" @click="handleUserList">测试获取用户请求</el-button>-->
    <router-view></router-view>
</template>

<script setup>
import { ref ,watch} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import store from "@/store";

const route=useRoute();
const router=useRouter();
const whitePath=['/login','/index','/']
watch(route,(to,from)=>{
    console.log("to"+to.name)
    console.log(to.path)
    if (whitePath.indexOf(to.path)===-1) {
        console.log("to.path="+to.path)
        let obj = {
            name: to.name,
            path: to.path
        }
        store.commit("ADD_TABS", obj)
    }
},{deep:true,immediate:true})
</script>
<style>
html,body,#app{
    height: 100%;
}
.app-container{
   padding: 20px;
}

     /* 默认样式，适用于大多数浏览器 */
 .myElement {
     appearance: none; /* 适用于 Chrome 84+, Chrome Android 84+, Edge 84+, Safari 15.4+, Safari on iOS 15.4+ */
 }

</style>
