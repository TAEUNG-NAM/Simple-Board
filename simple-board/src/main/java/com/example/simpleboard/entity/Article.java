package com.example.simpleboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity     // DB가 해당 객체를 인식!
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Article {

    @Id     // PK
    @GeneratedValue     // 자동 생성
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

}
