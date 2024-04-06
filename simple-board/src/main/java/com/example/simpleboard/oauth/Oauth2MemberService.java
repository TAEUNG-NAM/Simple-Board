package com.example.simpleboard.oauth;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class Oauth2MemberService extends DefaultOAuth2UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code를 리턴(OAuth-Client라이브러리) -> Access Token요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받음

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;    // 중복 가입 발생 방지 (ex google_1234, naver_1234)
        String email = oAuth2User.getAttribute("email");

        Member findMember = memberRepository.findByUsername(username);  // 가입을 위한 회원 조회
        if(findMember == null){   // DB에 존재하지 않을 시
            Member member = Member.builder()
                    .username(username)
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(username))
                    .role("ROLE_ADMIN")      // 일반 유저 권한 부여
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(member);
        }
        log.info("oAuth2User = " + oAuth2User.getAttributes());
        log.info("userRequest = " + userRequest.getClientRegistration());
        return oAuth2User;
    }
}
