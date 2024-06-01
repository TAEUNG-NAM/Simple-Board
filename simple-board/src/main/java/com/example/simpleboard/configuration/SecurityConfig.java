package com.example.simpleboard.configuration;

import com.example.simpleboard.jwt.CustomLogoutFilter;
import com.example.simpleboard.jwt.JWTFilter;
import com.example.simpleboard.jwt.JWTUtil;
import com.example.simpleboard.oauth.CustomSuccessHandler;
import com.example.simpleboard.oauth.Oauth2MemberService;
import com.example.simpleboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

// OAuth2 진행 순서
// 1. 코드받기(인증), 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보가져오기
// 4-1. 정보를 통해 회원가입 자동으로 진행, 4-2. 정보 + 추가 정보를 이용해 회원가입
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Oauth2MemberService oauth2MemberService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public SecurityConfig(Oauth2MemberService oauth2MemberService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, MemberRepository memberRepository){

        this.oauth2MemberService = oauth2MemberService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrfConfig) -> 
                        csrfConfig.disable())
                .cors((corsConfig) ->
                        corsConfig.disable())
                .httpBasic((httpBasic) ->
                        httpBasic.disable())
                .formLogin((formConfig) -> formConfig.disable())
                .logout((logoutConfig) -> logoutConfig.disable());

        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
        http
                .addFilterAt(new CustomLogoutFilter(jwtUtil, memberRepository), LogoutFilter.class);


        http    // 접근 범위 및 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers( "/", "/login", "/articles").permitAll()     //  로그인 페이지 이용 허용
                                .requestMatchers("/admin").hasRole("ADMIN")     //  관리자 권한 페이지
                                .anyRequest().authenticated());      // 모든 페이지 인증 필요


        http    // OAuth2 로그인 설정
                .oauth2Login((oauthLogin) ->
                        oauthLogin
                                .loginPage("/login")
                                .userInfoEndpoint((userInfoEndpointConfig ->    // 로그인 완료 후 회원 정보 받기
                                        userInfoEndpointConfig
                                                .userService(oauth2MemberService)))
                                .successHandler(customSuccessHandler));

        // Session Stateless 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
