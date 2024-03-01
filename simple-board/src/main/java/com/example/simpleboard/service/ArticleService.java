package com.example.simpleboard.service;

import com.example.simpleboard.dto.ArticleDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CommentRepository commentRepository;


    // GET
    public List<Article> indexAll() {
        return articleRepository.findAll();
    }

    // GET
    public Article index(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // POST
    @Transactional
    public ArticleDto create(ArticleDto dto) {
        Article article = dto.toEntity();
        if(article.getId() != null)
            return null;

        // Repository를 통해 DB에 저장
        Article created = articleRepository.save(article);

        // Article -> ArticleDto 변환 후 반환
        return ArticleDto.createArticleDto(created);
    }

    // PATCH
    @Transactional
    public ArticleDto update(Long id, ArticleDto dto) {
        // 수정용 엔티티 생성
        Article article = dto.toEntity();

        // 기존 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리(기존 데이터가 없거나, id 불일치)
        if (target == null || id != article.getId() || (article.getTitle()  == null && article.getContent() == null))
            return null;

        // 새로운 엔티티 패치 및 저장
        Article updated = target.patch(article);
        articleRepository.save(target);

        // Dto로 변환 후 반환
        return ArticleDto.createArticleDto(updated);
    }

    // DELETE
    @Transactional
    public ArticleDto delete(Long id) {
        // 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if(target == null)
            return null;

        // 게시글 삭제 시 작성된 댓글도 함께 삭제
        List<Comment> comments =  commentRepository.findByArticleId(id);    // 게시글ID를 통해 모든 댓글 불러오기
        List<Long> commentIds = comments.stream()       // stream() 문법을 통해 불러온 댓글들의 ID를 리스트로 저장
                .map(commentId -> commentId.getId())
                .collect(Collectors.toList());
        for(Long i : commentIds){       // 댓글ID가 담긴 리스트를 반복문을 통해 댓글Entity 호출 후 차례대로 삭제
            Comment comment = commentRepository.findById(i).orElse(null);
            commentRepository.delete(comment);
        }
        // 모든 댓글 삭제 후 엔티티 삭제 및 결과 반환
        articleRepository.delete(target);
        return ArticleDto.createArticleDto(target);
    }

    // 트랜잭션 테스트
    @Transactional
    public List<Article> createArticles(List<ArticleDto> dtos) {
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
