//package com.zjt.server.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private PasswordEncoder pw;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        //查询数据库判断用户名是否存在、
//        if(!"admin".equals(username)){
//            throw new UsernameNotFoundException("用户名不存在！");
//
//        }
//        //把查询出来的密码（已经加密）进行解析
//        String password = pw.encode("123456");
//        return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal"));
//    }
//}
