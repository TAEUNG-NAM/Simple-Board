package com.example.simpleboard.service;

import com.example.simpleboard.dto.MemberDto;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member findMember(String username){
        return memberRepository.findByUsername(username);
    }

}
