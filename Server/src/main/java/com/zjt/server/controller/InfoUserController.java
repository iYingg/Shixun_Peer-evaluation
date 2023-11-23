package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.PageBean;
import com.zjt.server.entity.PeerUser;
import com.zjt.server.entity.R;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller控制器
 */
@RestController
@RequestMapping("/info/user")
public class InfoUserController {

    @Autowired
    private PeerUserService peerUserService;

    /**
     * 修改用户信息
     * @param peerUser
     * @return
     */
    @PostMapping("/save")
    //@PreAuthorize("hasAnyAuthority('info:user:edit')")
    public R save(@RequestBody PeerUser peerUser){
        if(peerUser.getNo()==null){

        }
        else{
            peerUserService.updateById(peerUser);
        }
        return R.ok();
    }

    /**
     * 修改密码
     * @param peerUser
     * @return
     */
    @PostMapping("/resetPwd")
    //@PreAuthorize("hasAnyAuthority('info:user:resetPwd')")
    public R resetPwd(@RequestBody PeerUser peerUser){
        PeerUser currentUser = peerUserService.getByUsername(peerUser.getNo());
        currentUser.setPassword(peerUser.getNewPassword());
        peerUserService.updateById(currentUser);

        return R.ok();
    }

    /**
     * 根据学号查找数据
     * @param no
     * @return
     */
    @GetMapping("/{no}")
    public R findById(@PathVariable(value = "no")String no){
        PeerUser peerUser = peerUserService.getByUsername(no);
        Map<String,Object> map=new HashMap<>();
        map.put("peerUser",peerUser);
        return R.ok(map);
    }


    /**
     * 验证用户名，看是否重复
     * @param peerUser
     * @return
     */
    @PostMapping("/checkNo")
    public R checkUserName(@RequestBody PeerUser peerUser){
        if(peerUserService.getByUsername(peerUser.getNo())==null){
            return R.ok();
        }else{
            return R.error();
        }
    }

    /**
     * 验证Tno是否存在
      * @param peerUser
     * @return
     */
    @PostMapping("/checkTno")
    public R checkTno(@RequestBody PeerUser peerUser){

        if(peerUserService.getByUsername(peerUser.getNo()).getRole().equals(Integer.valueOf(3))){

            return R.ok();
        }else{
            return R.error();
        }
    }

}
