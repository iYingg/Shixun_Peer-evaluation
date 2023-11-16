package com.zjt.server.controller;



import com.zjt.server.entity.PeerUser;
import com.zjt.server.entity.R;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.util.JwtUtils;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PeerUserService testService;

    @RequestMapping("/user/list")
    //@PreAuthorize("hasRole('ROLE_admin')")
    //@PreAuthorize("hasAnyAuthority()")
    public R userList(@RequestHeader(required = false)String token){
        System.out.println(token);
        if(StringUtil.isNotEmpty(token)){
            Map<String, Object> resultmap = new HashMap<>();
            List<PeerUser>userList = testService.list();
            resultmap.put("userList",userList);
            return R.ok(resultmap);
        }else {
            return R.error(401,"token为空");
        }

    }

    @RequestMapping("/login")
    public R login(){
        String token = JwtUtils.genJwtToken("Zjt");
        return R.ok().put("token",token);
    }

//    @RequestMapping("/testHello")
//    public String test(){
//        return "Hello";
//    }
}
