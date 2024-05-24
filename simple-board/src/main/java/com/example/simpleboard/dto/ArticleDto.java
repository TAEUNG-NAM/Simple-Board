package com.example.simpleboard.dto;

import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String username;

    public static ArticleDto createArticleDto(Article created) {
        return new ArticleDto(
                created.getId(),
                created.getTitle(),
                created.getContent(),
                created.getMember().getUsername());
    }

    public Article toEntity(Member member) {

        return new Article(id, title, content, member);
    }
}
