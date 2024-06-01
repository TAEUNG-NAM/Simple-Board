package com.example.simpleboard.jwt;

import com.example.simpleboard.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        // 로그아웃 요청 uri 검증
        String uri = request.getRequestURI();
        if(!uri.matches("^/logout$")){
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 Method 검증
        String method = request.getMethod();
        if(!method.equals("POST")){
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키(토큰) 가져오기
        String access = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("access")){
                    access = cookie.getValue();
                }
            }
        }

        // Null Checking
        if(access == null){
            log.info("Access Token Null");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Expiration Checking
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e){
            log.info("Expired Access Token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Category Checking
        String category = jwtUtil.getCategory(access);
        if(!category.equals("access")){
            log.info("Mismatched Refresh Token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB Checking
        if(!memberRepository.existsByAccess(access)){
            log.info("Invalid Access Token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Delete Access Token in DB
        memberRepository.deleteByAccess(access);

        Cookie cookie = new Cookie("access", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("Logout Success!");
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
