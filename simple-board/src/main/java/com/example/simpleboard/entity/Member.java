package com.example.simpleboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Role;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;    // PK

    @Column(nullable = false)
    private String username;    // 이름

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Column(nullable = false)
    private String email;   // 이메일

    @Column(nullable = false)
    private String role;  // 유저 권한

    private String provider;    // 공급자(google, naver ..)

    private String providerId;  // 공급 아이디

    @Builder
    public Member(long id, String username, String password, String role, String email, String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }
}
