package com.zjt.server.service;

import com.zjt.server.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Tao
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2023-11-03 14:57:53
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);
}
