package com.example.simpleboard.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {
    private String username;
    private String name;
    private String role;
}
