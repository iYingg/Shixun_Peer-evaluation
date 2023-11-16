package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.SysMenu;
import com.zjt.server.service.SysMenuService;
import com.zjt.server.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tao
 * @description 针对表【sys_menu】的数据库操作Service实现
 * @createDate 2023-11-03 14:57:53
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        List<SysMenu> resultMenuList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            // 寻找子节点
            for (SysMenu e : sysMenuList) {
                if (e.getParentId() == sysMenu.getId()) {
                    sysMenu.getChildren().add(e);
                }
            }
            // 判断父节点，添加到集合
            if (sysMenu.getParentId() == 0L) {
                resultMenuList.add(sysMenu);
            }
        }
        return resultMenuList;

    }
}




