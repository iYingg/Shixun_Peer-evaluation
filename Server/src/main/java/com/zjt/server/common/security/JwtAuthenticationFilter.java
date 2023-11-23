package com.zjt.server.common.security;

import com.zjt.server.common.constant.JwtConstant;
import com.zjt.server.entity.CheckResult;
import com.zjt.server.entity.PeerUser;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.util.JwtUtils;
import com.zjt.server.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private PeerUserService peerUserService;

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    private static final String URL_WHITELIST[] = {
            "/test/login",
            "image/userAvatar/**",
            "/activ/publishhomework/**"
    };
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        System.out.println(request.getMethod()+token +"\n请求url："+request.getRequestURI());
        //如果token为空或者URL在白名单里则放行
        if(StringUtil.isEmpty(token)||new ArrayList<String>(Arrays.asList(URL_WHITELIST)).contains(request.getRequestURI())){
            chain.doFilter(request,response);
        }

        CheckResult checkResult = JwtUtils.validateJWT(token);
        if(!checkResult.isSuccess()){
            switch (checkResult.getErrCode()){
                case JwtConstant.JWT_ERRCODE_NULL:throw new JwtException("token不存在");
                case JwtConstant.JWT_ERRCODE_EXPIRE:throw new JwtException("token过期");
                case JwtConstant.JWT_ERRCODE_FAIL:throw new JwtException("token验证不通过");

            }
        }
        Claims claims = JwtUtils.parseJWT(token);
        String no = claims.getSubject();
        PeerUser peerUser = peerUserService.getByUsername(no);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(no,null,myUserDetailsService.getUserAuthority(peerUser.getNo()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);

    }

}
