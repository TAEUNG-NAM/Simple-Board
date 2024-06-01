package com.example.simpleboard.controller;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;
import com.example.simpleboard.service.MemberService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    MemberService memberService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 테스트 메인 페이지
    @GetMapping("/main")
    public @ResponseBody String success(){
        return "수민 생각~~";
    }


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
