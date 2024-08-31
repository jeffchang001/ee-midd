package com.sogo.ee.midd.security.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sogo.ee.midd.util.JwtTokenUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            if (jwtTokenUtil.validateToken(jwtToken)) {
                // Token 有效，设置认证信息
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    "user", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);
            }
        }

        chain.doFilter(request, response);
    }
}
