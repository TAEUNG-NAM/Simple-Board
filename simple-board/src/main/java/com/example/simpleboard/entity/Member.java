package com.example.simpleboard.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // PK

    @Column(nullable = false)
    private String userId;    // 이름

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Column(nullable = false)
    private String email;   // 이메일

    @Column(nullable = false)
    private String role;  // 유저 권한

    @Column(nullable = false)
    private String provider;    // 공급자(google, naver ..)

    @Column(nullable = false)
    private String providerId;  // 공급 아이디

    @Builder
    public Member(long id, String userId, String password, String email, String role, String provider, String providerId) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
