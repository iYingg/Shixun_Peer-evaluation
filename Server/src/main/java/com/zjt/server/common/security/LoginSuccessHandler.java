package com.zjt.server.common.security;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjt.server.entity.PeerUser;
import com.zjt.server.entity.R;
import com.zjt.server.entity.SysMenu;
import com.zjt.server.entity.SysRole;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.service.SysMenuService;
import com.zjt.server.service.SysRoleService;
import com.zjt.server.util.JwtUtils;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登陆成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private PeerUserService peerUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        String username = authentication.getName();
        String token = JwtUtils.genJwtToken(username);

        PeerUser currentUser = peerUserService.getByUsername(username);

//        // 更新用户最后登录记录
//        sysUserService.update(new UpdateWrapper<SysUser>().set("login_date",new
//                Date()).eq("username",username));
//        SysUser currentUser = sysUserService.getByUsername(username);
//        String token = JwtUtils.genJwtToken(username);
        //根据学号或者工号获取所有的角色信息
        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id","SELECT role_id FROM sys_user_role WHERE user_id = "+ Integer.parseInt(username)));

        //遍历所有的角色，获取所有菜单权限，而且不重复
        Set<SysMenu> menuSet = new HashSet<>();
        for (SysRole sysRole:roleList)
        {
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id","SELECT menu_id FROM sys_role_menu WHERE role_id = "+sysRole.getId()));
            for (SysMenu sysMenu:sysMenuList)
            {
                menuSet.add(sysMenu);
            }
        }
        switch (currentUser.getRole()){
            case 1:
                currentUser.setRole_cn("管理员");
                break;
            case 2:
                currentUser.setRole_cn("学生");
                break;
            case 3:
                currentUser.setRole_cn("老师");
                break;
            case 4:
                currentUser.setRole_cn("测试员");
                break;
        }


        List<SysMenu> sysMenuList=new ArrayList<>(menuSet);

        // 排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        List<SysMenu> menuList=sysMenuService.buildTreeMenu(sysMenuList);


        outputStream.write(JSONUtil.toJsonStr( R.ok("登陆成功").put("authorization",token).put("currentUser",currentUser).put("menuList",menuList)).getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
