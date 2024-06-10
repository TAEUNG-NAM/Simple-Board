package com.example.simpleboard.controller;

import com.example.simpleboard.dto.ArticleDto;
import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Member;
import com.example.simpleboard.oauth.CustomOAuth2User;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.service.ArticleService;
import com.example.simpleboard.service.CommentService;
import com.example.simpleboard.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class ArticleController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final ArticleRepository articleRepository;

    public ArticleController(CommentService commentService, MemberService memberService, ArticleRepository articleRepository) {
        this.commentService = commentService;
        this.memberService = memberService;
        this.articleRepository = articleRepository;
    }

    // 게시글 생성 페이지
    @GetMapping("/articles/new")
    public String newArticle(Model model){
        // 사용자 id 세션으로 부터 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("username", username);
        log.info("username: " + username);

        return "articles/new";
    }

    // 게시글 생성 메소드
    @PostMapping("/articles/create")
    public String createArticle(ArticleDto dto){

        // 1. DTO -> Entity 변환
        Member member = memberService.findMember(dto.getUsername()); // Article Entity -> DTO를 위한 유저정보 조회
        Article article = dto.toEntity(member);
        log.info(article.toString());

        // 2. Repository를 이용해 Entity를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        return "redirect:/articles";
    }

    // 특정 게시글 조회 메소드
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        // 0. 사용자 id 세션으로 부터 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.findMember(username);

        // 1. articleId로 DB에서 Entity를 호출
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

        // 2. 가져온 데이터를 모델에 등록
        model.addAttribute("member", member);   // 댓글 작성 및 수정에 이용
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentDtos);

        // 3. 페이지 반환
        return "articles/show";
    }

    // 모든 게시글 조회 메소드
    @GetMapping({"/", "/articles"})
    public String main(@CookieValue(value = "access", required = false, defaultValue = "0000") String access,
                       @RequestParam(name = "page", defaultValue = "1")Integer page,
                       @RequestParam(name = "title", defaultValue = "")String title,
                       Model model){
        // required 속성을 true로 지정 시, value 속성의 이름을 가진 쿠키가 존재하지 않을 시에 스프링 MVC는 익셉션을 발생시킨다.

        // 0. Page 설정(페이지 수, 정렬기준)
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        // 페이지 index 0부터 시작(view에서 1page 누르면 -1 값으로 index 호출)
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts)); // (현재페이지, 게시글 갯수[한페이지] 정렬기준)

        // 1. 모든 article을 가져온다
        Page<Article> articleEntityList = articleRepository.findByTitleContaining(title, pageable);

        // 2-1. Page의 시작과 끝 계산
        int endPage = Math.min(articleEntityList.getTotalPages(), (int)Math.floor((articleEntityList.getPageable().getPageNumber()+10)/10.0)*10);   // (현재 페이지 + 10)1의 자리 내림 = 끝 페이지(10, 20, 30...)
        int startPage = Math.max(1, endPage-9);
        List<Integer> pages = new ArrayList<>();
        for(int i = startPage; i <= endPage; i++){
            pages.add(i);   // 첫 페이지부터 끝 페이지(ex 1~10, 11~20, 21~30)
        }
        // 2-2. View로 전달
        model.addAttribute("title", title);
        model.addAttribute("pages", pages); // 페이지 번호가 담긴 List(1~10, 11~20, 21~30 .. )
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber()+1);   // 이전 페이지 or 첫 페이지(0)
        model.addAttribute("next", pageable.next().getPageNumber()+1);  // 다음 페이지
        model.addAttribute("hasNext", articleEntityList.hasNext()); // 다음 게시글 유무 체크

        // 3. 가져온 article 묶음, Cookie(Token) 뷰로 전달
        model.addAttribute("accessCookie", access);
        model.addAttribute("articleList", articleEntityList);

        // 4. 페이지 반환
        return "articles/main";
    }

    // 게시글 수정 메소드 #1
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 현재 사용자 id 세션으로 부터 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 수정할 데이터 호출
        Article target = articleRepository.findById(id).orElse(null);

        if(!username.equals(target.getMember().getUsername())){
            log.info("접근 권한 없음");
            return "redirect:/articles";
        }

        // 모델에 데이터 등록
        model.addAttribute("article", target);

        // 페이지 반환
        return "articles/edit";
    }

    // 게시글 수정 메소드 #2 (페이지 form에서 받은 데이터)
    @PostMapping("/articles/update")
    public String update(ArticleDto dto){
        Member member = memberService.findMember(dto.getUsername());     // Article Entity -> DTO를 위한 유저정보 조회
        // Dto를 Entity로 변환
        Article article = dto.toEntity(member);
        log.info(article.toString());

        // Entity를 DB에 갱신
        Article target = articleRepository.findById(article.getId()).orElse(null);
        if(target != null){
            articleRepository.save(article);
        }

        // 수정된 페이지 반환
        return "redirect:/articles";
    }

    // 게시글 삭제 메소드
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){

        // 삭제할 데이터를 불러온다
        Article target = articleRepository.findById(id).orElse(null);

        // 데이터가 존재하면 삭제
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
        }

        // 페이지 반환
        return "redirect:/articles";
    }
}