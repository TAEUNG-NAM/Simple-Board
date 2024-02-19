package com.example.simpleboard.service;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ArticleRepository articleRepository;

    // 댓글 조회(GET)
    public List<CommentDto> comments(Long articleId) {
        // 대상 게시글의 댓글 조회
        List<CommentDto> dtos = commentRepository.findByArticleId(articleId)
                                .stream()
                                .map(comment -> CommentDto.createCommentDto(comment))
                                .collect(Collectors.toList());

        // 결과 반환
        return dtos;
    }

    @Transactional
    // 댓글 생성(POST)
    public CommentDto create(Long articleId, CommentDto dto) {
        // 게시글 조회 및 에러 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 존재하지 않는 게시글입니다."));

        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article);

        // DB에 저장
        Comment created = commentRepository.save(comment);

        // DTO로 변환 후 결과 반환
        return CommentDto.createCommentDto(created);
    }


    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 댓글 조회 및 에러 발생
        Comment target = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글 수정 실패! 존재하지 않는 댓글입니다.")
        );

        // 댓글 수정
        Comment updated = target.patch(dto);

        // DB 저장
        commentRepository.save(updated);

        // DTO로 변환 후 결과 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) {
        // 댓글 조회 및 에러 발생
        Comment target = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글 삭제 실패! 해당 댓글이 존재하지 않습니다.")
        );

        // 댓글 삭제
        commentRepository.delete(target);

        // 결과 반환
        return CommentDto.createCommentDto(target);
    }
}
