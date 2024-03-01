package com.example.simpleboard.dto;

import com.example.simpleboard.entity.Article;
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

    public static ArticleDto createArticleDto(Article created) {
        return new ArticleDto(
                created.getId(),
                created.getTitle(),
                created.getContent());
    }

    public Article toEntity() {
        return new Article(id, title, content);
    }
}
