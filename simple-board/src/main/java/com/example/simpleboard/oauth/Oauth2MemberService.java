package com.example.simpleboard.oauth;

import com.example.simpleboard.dto.*;
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

@Service
@Slf4j
public class Oauth2MemberService extends DefaultOAuth2UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);    // oAuth2User으로 부터 attribute 추출
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code를 리턴(OAuth-Client라이브러리) -> Access Token요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받음

        String provider = userRequest.getClientRegistration().getRegistrationId();  // OAuth2 서비스 제공자(ex google, naver, kakao)

        OAuth2Response oAuth2Response = null;
        if(provider.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if(provider.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else{
            return null;
        }

        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();    // 중복 가입 발생 방지 (ex google_1234, naver_1234)

        // DB 조회 후 강제 회원 가입
        Member findMember = memberRepository.findByUsername(username);  // 가입을 위한 회원 조회
        if(findMember == null){   // DB에 존재하지 않을 시
            Member member = Member.builder()
                            .username(username)     // provider_providerId (ex naver_12345, google_98765)
                            .name(oAuth2Response.getName())
                            .email(oAuth2Response.getEmail())
                            .role("ROLE_USER")      // 일반 유저 권한 부여
                            .build();
            memberRepository.save(member);

            // memberDto를 담아서 로그인 진행
            MemberDto dto = new MemberDto(username, oAuth2Response.getName(), "ROLE_USER");
            return new CustomOAuth2User(dto);

        } else{
            // Entity 수정 및 저장
            findMember.setEmail(oAuth2Response.getEmail());
            findMember.setName(oAuth2Response.getName());
            memberRepository.save(findMember);

            // memberDto를 담아 로그인 진행
            MemberDto dto = new MemberDto(username, oAuth2Response.getName(), "ROLE_USER");
            return new CustomOAuth2User(dto);
        }
    }
}
