import router from "@/router/index"
import store from "@/store"

router.beforeEach((to,from,next)=>{
    const whiteList=['/login'] // 白名单
    let token=sessionStorage.getItem("token");
    let hasRoutes=store.state.hasRoutes;
    let menuList=JSON.parse(sessionStorage.getItem("menuList"));

    if(token){
        console.log("token对了" + hasRoutes);
        if(!hasRoutes){
            console.log("动态绑定");
            bindRoute(menuList);
            store.commit("SET_ROUTES_STATE",true);
        }
        next();
    }else{
        if(whiteList.includes(to.path)){
            next();
        }else{
            console.log("重新登陆一下");
            next("/login");
        }
    }
})


// 动态绑定路由
const bindRoute=(menuList)=>{
    console.log(menuList)

    let newRoutes=router.options.routes;
    if(newRoutes[0].children == undefined)
        newRoutes[0].children = [];
    menuList.forEach(menu=>{
        if(menu.children){
            menu.children.forEach(m=>{
// 菜单转成路由
                let route=menuToRoute(m,menu.name);
                if(route){

                    newRoutes[0].children.push(route); // 添加到路由管理
                }
            })
        }
    })
// 重新添加到路由
    newRoutes[0].children.forEach(route=>{
        console.log("add " + route.name + route.path);
        router.addRoute('首页',route);
    })
}
//菜单转成路由
const menuToRoute = (menu,parentName) => {
    if (menu.component == "1") {
        console.log(menu.name +"无")
        return null
    }else{
        let route = {
            name: menu.name,
            path: menu.path,
            meta:{
                parentName:parentName
            }
        }
        console.log(menu.name+"转路由"+menu.path)
        route.component = () => import('@/views/' + menu.component +'.vue')
        return route
    }
}