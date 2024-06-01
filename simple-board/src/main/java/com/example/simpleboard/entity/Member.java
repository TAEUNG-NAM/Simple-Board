package com.example.simpleboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;    // PK

    @Id
    @Column(nullable = false, unique = true)
    private String username;    // 아이디

    private String name;    // 실명

    @Column(nullable = false)
    private String email;   // 이메일

    @Column(nullable = false)
    private String role;  // 유저 권한

    private String access;

    @Transactional
    public void setAccess(String token){
        this.access = token;
    }
}
