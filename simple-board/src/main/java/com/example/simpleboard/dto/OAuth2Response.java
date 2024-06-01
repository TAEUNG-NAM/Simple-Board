package com.example.simpleboard.dto;


public interface OAuth2Response {
    // 서비스 제공자(google, naver, kakao)
    String getProvider();

    // 서비스 제공자ID
    String getProviderId();

    // 서비스에 등록된 이메일
    String getEmail();

    // 서비스에 등록된 실명
    String getName();
}
