<template>
    <el-button type="danger" @click="test">测试</el-button>
    <el-button type="danger" @click="test2">测试loginout</el-button>
</template>

<script setup>
import requestUtil from '@/util/request'
import {ref, onMounted, watch, watchEffect} from "vue";

const test = async ()=>{
    let result = await requestUtil.get("test/user/list");
}
const test2 = async ()=>{
    let result = await requestUtil.get("logout");
}




</script>

<style scoped>

</style>