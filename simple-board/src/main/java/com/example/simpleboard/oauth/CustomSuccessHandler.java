package com.example.simpleboard.oauth;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.jwt.JWTUtil;
import com.example.simpleboard.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // authentication = 인증된 사용자의 세부사항을 보유하는 인증 객체(유저정보, 권한 등 보유)

        // 인증된 사용자 세부정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        // 아이디 가져오기
        String username = customUserDetails.getUsername();

        // 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Access 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 3600000L);  // 1시간(ms)

        // DB에 토큰 저장(기존 데이터 삭제)
        Member member = memberRepository.findByUsername(username);
        member.setAccess(access);
        memberRepository.save(member);
        
        // Access토큰 쿠키로 전달(OAuth2 정책 상 Header 전달x)
        response.addCookie(createCookie("access", access));
        
        // 응답 코드(200) 반환 및 리다이렉트
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/articles");

    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);  // (60번 * 60초) == 1시간
        cookie.setHttpOnly(true);   // 자바스크립트 차단
//        cookie.setSecure(true);   // Https 통신

        return cookie;
    }
}
