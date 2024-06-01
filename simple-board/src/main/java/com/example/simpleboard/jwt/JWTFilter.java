package com.example.simpleboard.jwt;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.oauth.CustomOAuth2User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키로 부터 Access Token 가져오기
        String access = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("access")){
                    access = cookie.getValue();
                }
            }
        }

        // Null Checking
        if(access == null){
            log.info("Access Token Null");
            filterChain.doFilter(request, response);
            return;
        }

        // Expiration Checking
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            log.info("Expired Access Token");
            PrintWriter writer = response.getWriter();
            writer.println("Expired Access Token");

            // 상태 코드 응답(401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Category Checking
        if(!jwtUtil.getCategory(access).equals("access")){
            log.info("Mismatched Access Token");
            PrintWriter writer = response.getWriter();
            writer.println("Mismatched Access Token");

            // 상태 코드 응답(401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }

        // Access에서 유저 정보 획득
        String username = jwtUtil.getUsername(access);
        String role = jwtUtil.getRole(access);

        // 임시 DTO 생성
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(username);
        memberDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
