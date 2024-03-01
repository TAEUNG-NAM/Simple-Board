package com.example.simpleboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity     // DB가 해당 객체를 인식!
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Article {

    @Id     // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 자동 생성(DB에서 id 자동 생성)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    public Article patch(Article article) {
        if(article.getTitle() != null)
            this.title = article.getTitle();
        if(article.getContent() != null)
            this.content = article.getContent();
        return this;
    }

}
