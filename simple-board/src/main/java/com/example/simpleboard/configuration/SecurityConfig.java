package com.example.simpleboard.configuration;

import com.example.simpleboard.oauth.Oauth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// OAuth2 진행 순서
// 1. 코드받기(인증), 2. 엑세스토큰(권한)
// 3. 사용자 프로필 정보가져오기
// 4-1. 정보를 통해 회원가입 자동으로 진행, 4-2. 정보 + 추가 정보를 이용해 회원가입
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final Oauth2MemberService oauth2MemberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable())
//                .headers(headers ->
//                        headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))    // h2console 이용을 위함
                .cors((corsconfig) ->
                        corsconfig.disable())
                .httpBasic((httpBasic) ->
                        httpBasic.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
//                                .requestMatchers(PathRequest.toH2Console()).permitAll()     // h2console 이용 허용
                                .requestMatchers("/login/**","/loginForm/**","/join","/joinProc").permitAll()     //  로그인 페이지 이용 허용
                                .requestMatchers("/admin").hasRole("ADMIN")     //  관리자 권한 페이지
                                .anyRequest().permitAll())      // 모든 페이지 인증 필요
                .exceptionHandling(handling ->
                        handling.accessDeniedPage("/articles"))    // 접근 거부 페이지 설정(관리자 페이지 젒근)
                .logout((logout) -> logout
                        .logoutSuccessUrl("/loginForm"))    // 로그아웃 시 로그인 페이지로 이동
                .formLogin(formL ->
                        formL
                                .loginPage("/loginForm")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/articles"))
                .oauth2Login((oauthLogin) ->
                        oauthLogin
                                .loginPage("/loginForm")
                                .defaultSuccessUrl("/articles")     // 로그인 성공 시 메인 페이지로 이동
                                .userInfoEndpoint((userInfoEndpointConfig ->    // 로그인 완료 후 회원 정보 받기
                                        userInfoEndpointConfig
                                                .userService(oauth2MemberService))));

        return http.build();
    }
}
