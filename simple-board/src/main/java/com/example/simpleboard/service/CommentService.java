package com.example.simpleboard.service;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.repository.CommentRepository;
import com.example.simpleboard.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }


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
        // 게시글 조회 및 에러 처리
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 존재하지 않는 게시글입니다."));

        Member member = memberRepository.findByUsername(dto.getUsername());

        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article, member);

        // DB에 저장
        Comment created = commentRepository.save(comment);
        log.info("댓글 생성 완료!");

        // DTO로 변환 후 결과 반환
        return CommentDto.createCommentDto(created);
    }


    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 댓글 조회
        Comment target = commentRepository.findById(id).orElse(null);
        if(target == null){
            log.info("댓글 수정 실패! 존재하지 않는 댓글입니다.");
            return null;
        }

        // 쿠키를 통해서 현재 사용자 정보 획득
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 사용자 정보와 댓글 정보 비교 및 에러 처리
        if(!target.getMember().getUsername().equals(dto.getUsername()) || !target.getMember().getUsername().equals(username)){
            log.info("댓글 수정 실패! 수정 권한 없음");
            return null;
        }

        // 댓글 수정
        Comment updated = target.patch(dto);

        // DB 저장
        commentRepository.save(updated);

        // DTO로 변환 후 결과 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) throws AccessDeniedException {
        // 댓글 조회 및 에러 처리(400)
        Comment target = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("댓글 삭제 실패! 해당 댓글이 존재하지 않습니다.")
        );

        // 쿠키를 통해서 현재 사용자 정보 획득
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 사용자 정보와 댓글 정보 비교 및 에러 처리(403)
        if(!target.getMember().getUsername().equals(username))
            throw new AccessDeniedException("댓글 삭제 실패. 권한이 없습니다.");

        // 댓글 삭제
        commentRepository.delete(target);

        // 결과 반환
        return CommentDto.createCommentDto(target);
    }
}
