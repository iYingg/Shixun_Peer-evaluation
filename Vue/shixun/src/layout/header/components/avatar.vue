<template>
<el-dropdown>
<span class="el-dropdown-link">
<el-avatar  :size="40" :src="image" />
&nbsp;&nbsp;{{ currentUser.name }}
<el-icon class="el-icon--right">
<arrow-down />
</el-icon>
</span>
<template #dropdown>
    <el-dropdown-menu>
        <el-dropdown-item>
            <router-link :to="{name:'个人信息管理'}">个人中心</router-link>
        </el-dropdown-item>
        <el-dropdown-item @click="logout">安全退出</el-dropdown-item>
    </el-dropdown-menu>
</template>
</el-dropdown>
</template>
<script setup>
import { ArrowDown } from '@element-plus/icons-vue'
import {ref} from 'vue'
import store from '@/store'
import requestUtil,{getServerUrl} from '@/util/request'
const currentUser=ref(JSON.parse(sessionStorage.getItem("userInfo")));

const image = ref(require("@/assets/images/avatar.jpg"))
const logout=async ()=>{
    let result=await requestUtil.get("/logout");
    if(result.data.code===200){
        store.commit("SET_ROUTES_STATE",false);
        store.commit("RESET_TABS");
        await store.dispatch('logout')
    }
}
</script>
<style lang="scss" scoped>
.el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>