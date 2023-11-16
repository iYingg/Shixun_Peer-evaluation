package com.zjt.server.common.security;

import com.zjt.server.entity.PeerUser;
import com.zjt.server.service.PeerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PeerUserService peerUserService;

    @Autowired
    private PasswordEncoder pw;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PeerUser peeruser = peerUserService.getByUsername(username);
        if(peeruser==null){

            throw new UsernameNotFoundException("用户名或密码错误！");

        }

        //System.out.println(peeruser.getNo()+pw.encode(peeruser.getPassword()));
        return new User(peeruser.getNo(),pw.encode(peeruser.getPassword()),getUserAuthority(peeruser.getNo()));
    }

    public List<GrantedAuthority> getUserAuthority(String NO) {
        String authority = peerUserService.getUserAuthorityInfo(NO);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
