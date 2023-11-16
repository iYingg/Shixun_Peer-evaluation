<template>

    <div class="login-page">
        <div class="login-header">

        </div>
        <div class="login-panel">
            <div class="logo">
                <el-image :src="logoImage" class="logo-image"></el-image>
            </div>
            <div class="login-text">
                <div>同伴互评用户登录</div>
            </div>
            <div class="form">
                <el-form :model="loginFormDate" :label-width = "100" ref="loginRef" :rules="loginRules">
                    <el-form-item label="学号/工号:" prop="username">
                        <el-input prefix-icon="Avatar" v-model="loginFormDate.username" placeholder="请输入学号/工号" ></el-input>
                    </el-form-item>
                    <el-form-item label="密码:" prop="password" >
                        <el-input prefix-icon="Unlock" v-model="loginFormDate.password" placeholder="请输入密码"
                                  type="password"
                                  show-password></el-input>
                    </el-form-item>
<!--                   <el-form-item class="forget">-->
<!--                       <el-button-->
<!--                           v-for="button in buttons"-->
<!--                           :key="button.text"-->
<!--                           :type="button.type"-->
<!--                           link-->
<!--                       >{{ button.text }}</el-button>-->
<!--                   </el-form-item>-->
                    <el-form-item class="button_login">
                        <el-button type="primary" @click="handleLogin" size="large"><span>登陆</span></el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div class="login-footer">
            版权：啊对对对队_Zjt
        </div>
    </div>
</template>


<script setup>
    import {reg} from 'vue'
    import requestUtil from "@/util/request";
    import store from "@/store";
    import qs from 'qs'
    import router from "@/router";

    const loginRef = ref(null);



    //logo变量
    import {ref,reactive} from "vue";
    import {ElMessage} from "element-plus";

    const logoImage = ref(require('@/assets/logo.png'))
    //登陆表单数据
    const loginFormDate = reactive({
        username: '',
        password: ''
    })

    const buttons = [
        {
            text: '忘记密码',
            type: ''
        }
    ]

    const loginRules ={
        username: [
            { required: true, message: '请输入学号/工号', trigger: 'blur' },
            { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
        ]
    }

    const handleLogin =  ()=>{
        loginRef.value.validate(async(valid)=>{
            if(valid){
                let result = await requestUtil.post("login?"+qs.stringify(loginFormDate));
                let data = result.data;
                if(data.code==200){
                    const token = data.authorization
                    const menuList = data.menuList;
                    const currentUser = data.currentUser;
                    //console.log("menulist:"+menuList);

                    const setPromise = new Promise((resolve, reject) => {
                        store.commit('SET_MENULIST',menuList);
                        store.commit('SET_TOKEN',token);
                        store.commit('SET_TOKEN',token);
                        store.commit('SET_USERINFO',currentUser);
                        console.log("commit了"+token +sessionStorage.getItem("token"));
                        resolve();
                    });
                    //console.log("token:"+store.getters.GET_TOKEN);
                    await setPromise.then(() => {
                        router.replace("/home");
                    });

                }else{
                    ElMessage.error(data.msg)
                }
            }else {
                console.log("验证失败")
            }
        //     if(!valid){
        //         console.log("表单验证失败");
        //     }
        //     let result = await requestUtil.post("login?"+qs.stringify(loginFormDate));
        //     console.log(result.data);
        //
         })
    }


</script>



<style scoped = "scoped">


.login-page {
    width: 100%;
    height: 100%;
    background-image: url("../assets/images/background.JPEG");
    background-repeat: no-repeat;
    background-size: 100% 100%;
    position: absolute;

    top: 0;
    left: 0;
}
.login-page .login-panel{
    width: 40%;
    height: 55%;
    background-color: #fdfdfe;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
    border-radius: 10px;
    box-shadow: 0 0 10px #000;
    opacity: 0.75;
}

.login-page .login-panel .logo{
    width: 50%;
    height: 20%;
    text-align: center;
    padding-top: 20px;
}

.login-page .login-panel .logo .logo-image{
    width: 50%;
    height: 100%;
    margin: 0 0 0 230px;
}

.login-page .login-panel .login-text{
    width: 100%;
    height: 10%;
    text-align: center;
    font-size: 25px;
    font-weight: bold;
    padding-top: 20px;
}

.login-page .login-panel .form >>> .el-form-item__label{
    font-size: 16px;
    font-weight: bold;
    margin: 0 0 0 15%;
}

.login-page .login-panel .form >>> .el-input__prefix-inner{
    color: #0c0c0c;
}

.login-page .login-panel .form >>> .el-input{
    font-size: 16px;
    width: 70%;
    margin: 0 0 0 0;

}

.login-page .login-panel .form .forget >>> .el-button{

    font-size: 16px;
    margin: 0 0 0 60%;
}

.login-page .login-panel .form .button_login >>> .el-button{
    font-size: 16px;
    width: 40%;
    margin: 0 0 0 20%;

}

.login-page .login-footer{
    width: 100%;
    height: 5%;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translate(-50%,0);
    color: #1c1c1e;
}


</style>
