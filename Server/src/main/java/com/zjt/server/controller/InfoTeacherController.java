package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.PageBean;
import com.zjt.server.entity.PeerUser;
import com.zjt.server.entity.R;
import com.zjt.server.entity.SysUserRole;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.service.SysUserRoleService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/info/teacher")
public class InfoTeacherController {


    @Autowired
    private PeerUserService peerUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 教师信息列表
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        Page<PeerUser> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = peerUserService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<PeerUser>
                    ().like("role",3));
        }else {
            pageResult = peerUserService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<PeerUser>
                    ().inSql("no","select no from  peer_user where role=3 and name like '%"+query+"%'"));
        }

        List<PeerUser> userList = pageResult.getRecords();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("userList",userList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    /**
     * 添加或修改
     * @param peerUser
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody PeerUser peerUser){
        System.out.println(peerUser.toString());
        if(peerUser.getId()==0 || peerUser.getId()==-2){
            peerUserService.insertTeacher(peerUser);
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(Long.parseLong(peerUser.getNo()));
            sysUserRole.setRoleId(3L);
            sysUserRoleService.saveOrUpdate(sysUserRole);

        }else{
            peerUserService.updateById(peerUser);
        }
        return R.ok();
    }


    /**
     * 删除
     * @param sno
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    public R delete(@RequestBody String sno){
        sno = sno.substring(0,sno.length()-1);
        peerUserService.removeTea(sno);
        return R.ok();
    }
}
