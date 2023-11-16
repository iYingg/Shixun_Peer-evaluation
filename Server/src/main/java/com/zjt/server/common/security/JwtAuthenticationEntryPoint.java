package com.zjt.server.common.security;

import cn.hutool.json.JSONUtil;
import com.zjt.server.entity.R;
import com.zjt.server.util.JwtUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt自定义认证失败处理
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();

        outputStream.write(JSONUtil.toJsonStr( R.error(HttpServletResponse.SC_UNAUTHORIZED,"认证失败，请登录!")).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
