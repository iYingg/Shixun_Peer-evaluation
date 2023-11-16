package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.PeerUser;
import com.zjt.server.entity.SysMenu;
import com.zjt.server.entity.SysRole;
import com.zjt.server.entity.SysRoleMenu;
import com.zjt.server.mapper.SysMenuMapper;
import com.zjt.server.mapper.SysRoleMapper;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.mapper.PeerUserMapper;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Tao
* @description 针对表【peer_user】的数据库操作Service实现
* @createDate 2023-10-31 23:39:31
*/
@Service
public class PeerUserServiceImpl extends ServiceImpl<PeerUserMapper, PeerUser>
    implements PeerUserService{

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Autowired
    PeerUserMapper peerUserMapper;

    @Override
    public PeerUser getByUsername(String username) {
        return getOne(new QueryWrapper<PeerUser>().eq("NO",username));
    }

    @Override
    public String getUserAuthorityInfo(String no) {
        StringBuffer authority = new StringBuffer();
        //根据学号或者工号获取所有的角色信息
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id","SELECT role_id FROM sys_user_role WHERE user_id = "+ Integer.parseInt(no)));
        if(roleList.size()>0)
        {
            String roleCodeStrs = roleList.stream().map(r->"Role_"+r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStrs);
        }
        //遍历所有的角色，获取所有菜单权限，而且不重复
        Set<String> menuCodeSet = new HashSet<>();
        for (SysRole sysRole:roleList)
        {
            List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id","SELECT menu_id FROM sys_role_menu WHERE role_id = "+sysRole.getId()));
            for (SysMenu sysMenu:sysMenuList)
            {
                String perms = sysMenu.getPerms();
                if(StringUtil.isNotEmpty(perms))
                {
                    menuCodeSet.add(perms);
                }
            }
        }
        if (menuCodeSet.size()>0)
        {
            authority.append(",");
            String menuCodeStrs = menuCodeSet.stream().collect(Collectors.joining(","));
            authority.append(menuCodeStrs);
        }
        System.out.println(authority.toString());
        return authority.toString();
    }

    @Override
    public void insertStudent(PeerUser peerUser) {
        peerUserMapper.insertStudent(peerUser);
    }

    @Override
    public void removeStu(String sno) {
        peerUserMapper.removeStu(sno);
    }

    @Override
    public void insertTeacher(PeerUser peerUser) {
        peerUserMapper.insertTeacher(peerUser);
    }

    @Override
    public void removeTea(String sno) {
        peerUserMapper.removeStu(sno);
    }
}




