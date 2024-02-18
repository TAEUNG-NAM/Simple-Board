package com.example.simpleboard.service;

import com.example.simpleboard.dto.ArticleForm;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;


    // GET
    public List<Article> indexAll() {
        return articleRepository.findAll();
    }

    // GET
    public Article index(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // POST
    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if(article.getId() != null)
            return null;
        return articleRepository.save(article);
    }

    // PATCH
    public Article update(Long id, ArticleForm dto) {
        // 수정용 엔티티 생성
        Article article = dto.toEntity();

        // 기존 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리(기존 데이터가 없거나, id 불일치)
        if (target == null || id != article.getId() || (article.getTitle()  == null && article.getContent() == null))
            return null;

        // 새로운 엔티티 저장 및 결과 반환
        target.patch(article);
        return articleRepository.save(target);
    }

    // DELETE
    public Article delete(Long id) {
        // 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if(target == null)
            return null;

        // 엔티티 삭제 및 결과 반환
        articleRepository.delete(target);
        return target;
    }

    // 트랜잭션 테스트
    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // Dto 묶음을 Entity로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());

        // Entity 묶음을 DB에 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결재 실패!")
        );

        // 결과값 반환
        return articleList;
    }
}
