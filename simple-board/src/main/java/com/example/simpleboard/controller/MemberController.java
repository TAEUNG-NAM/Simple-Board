package com.example.simpleboard.controller;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;    // 비밀번호 암호화

    // 로그인 페이지
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String join(){
        return "join";
    }

    // 회원가입 프로세스
    @PostMapping("/joinProc")
    public String joinProc(Member member){

        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole("ROLE_ADMIN");
        memberRepository.save(member);

        return "redirect:loginForm";
    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
}
