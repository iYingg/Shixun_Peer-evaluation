import { createStore } from 'vuex'
import router from "@/router";
export default createStore({
  state: {
    hasRoutes:false,
    token:1,
    menuList:undefined,
    userInfo:undefined,
    editableTabsValue:"/home/index",
    editableTabs:[
      {
        title:'主页',
        name:'/home/index'
      }
    ]
  },
  // getters: {
  //   GET_TOKEN: state => {
  //     return sessionStorage.getItem("token")
  //   },
  //   GET_MENULIST: state => {
  //     return JSON.parse(sessionStorage.getItem("menuList"))
  //   },
  //   GET_USERINFO: state => {
  //     return JSON.parse(sessionStorage.getItem("userInfo"))
  //   },
  // },
  mutations: {
    SET_TOKEN:(state, token)=>{
        sessionStorage.setItem("token",token)
    },
    SET_MENULIST:(state, menuList)=>{
      sessionStorage.setItem("menuList",JSON.stringify(menuList))
    },
    SET_USERINFO:(state, userInfo)=>{
      sessionStorage.setItem("userInfo",JSON.stringify(userInfo))
    },
    SET_ROUTES_STATE:(state, hasRoutes)=>{
      state.hasRoutes=hasRoutes
    },
    ADD_TABS:(state,tab)=>{
      if(state.editableTabs.findIndex(e=>e.name===tab.path)===-1){
        state.editableTabs.push({
          title:tab.name,
          name:tab.path
        })
      }
      state.editableTabsValue=tab.path
    },
    RESET_TABS:(state)=>{
      state.editableTabsValue='/home/index';
      state.editableTabs=[
        {
          title:'首页',
          name:'/home/index'
        }
      ]
    },
  },
  actions: {
    //安全退出
    logout(){
      window.sessionStorage.clear();
      router.replace("/login")
    }
  },
  modules: {
  }
})
