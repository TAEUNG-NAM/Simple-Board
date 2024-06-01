package com.example.simpleboard.service;

import com.example.simpleboard.dto.ArticleDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.repository.CommentRepository;
import com.example.simpleboard.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;


    // GET
    //@Transactional(readOnly = true) : readOnly = true를 추가하면 트랜잭션 범위는 추가하되,
    //조회기능만 남겨두어 조회속도가 개선됨. 등록,수정,삭제가 없는 서비스메소드에 사용하는것을 추천
    @Transactional(readOnly = true)
    public List<Article> indexAll() {
        return articleRepository.findAll();
    }

    // GET
    @Transactional(readOnly = true)
    public Article index(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // POST
    @Transactional
    public ArticleDto create(ArticleDto dto) {
        Member member = memberRepository.findByUsername(dto.getUsername()); // Article Entity -> DTO를 위한 유저정보 조회
        Article article = dto.toEntity(member);
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
        // 쿠키를 통해서 현재 사용자 정보 획득
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Username : {}", username);

        // 기존 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 세션의 정보와 기존 Article 작성자가 다르면 수정 거부
        if(!target.getMember().getUsername().equals(username)){
            log.info("수정 권한 없음");
            return null;
        }

        // 수정용 엔티티 생성
        Member member = memberRepository.findByUsername(username); // Article Entity -> DTO를 위한 유저정보 조회
        Article article = dto.toEntity(member);


        // 잘못된 요청 처리(기존 데이터가 없거나, id 불일치)
        if (target == null || id != article.getId() || (article.getTitle()  == null && article.getContent() == null)){
            log.info("잘못된 요청 처리(기존 데이터가 없거나, articleId 불일치)");
            return null;
        }

        // 새로운 엔티티 패치 및 저장
        Article updated = target.patch(article);
        articleRepository.save(target);

        // Dto로 변환 후 반환
        return ArticleDto.createArticleDto(updated);
    }

    // DELETE
    @Transactional
    public ArticleDto delete(Long id) {
        // 사용자 id 세션(쿠키)으로 부터 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if(target == null || !username.equals(target.getMember().getUsername())){
            log.info("존재하지 않는 게시글 or 삭제 권한 없음");
            return null;
        }

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
        log.info("게시글 삭제 완료!");
        return ArticleDto.createArticleDto(target);
    }

//    // 트랜잭션 테스트
//    @Transactional
//    public List<Article> createArticles(List<ArticleDto> dtos) {
//        // Dto 묶음을 Entity로 변환
//        List<Article> articleList = dtos.stream()
//                .map(dto -> dto.toEntity())
//                .collect(Collectors.toList());
//
//        // Entity 묶음을 DB에 저장
//        articleList.stream()
//                .forEach(article -> articleRepository.save(article));
//
//        // 강제 예외 발생
//        articleRepository.findById(-1L).orElseThrow(
//                () -> new IllegalArgumentException("결재 실패!")
//        );
//
//        // 결과값 반환
//        return articleList;
//    }
}
