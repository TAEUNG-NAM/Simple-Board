package com.example.simpleboard.controller;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;
import com.example.simpleboard.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;    // 비밀번호 암호화

//    // 로그인 페이지
//    @GetMapping("/login")
//    public String loginForm() {
//        return "login";
//    }
//
//    // 회원가입 페이지
//    @GetMapping("/join")
//    public String join(){
//        return "join";
//    }
//
//    // 회원가입 프로세스(서비스 계층을 통한 분리 필수!)
//    @PostMapping("/joinProc")
//    public String joinProc(MemberDto memberDto){
//        memberService.joinProcess(memberDto);
//        return "redirect:login";
//    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    // 접근 제한 페이지
    @GetMapping("/denied")
    public String deniedP(){
        return "denied";
    }
}
