package com.example.simpleboard.controller;

import com.example.simpleboard.dto.ArticleForm;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.repository.ArticleRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    // 게시글 생성 페이지
    @GetMapping("/articles/new")
    public String newArticle(){
        return "articles/new";
    }

    // 게시글 생성 메소드
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){

        // 1. DTO -> Entity 변환
        Article article = form.toEntity();
        log.info(article.toString());

        // 2. Repository를 이용해 Entity를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        return "redirect:/articles";
    }

    // 게시글 조회 메소드
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        // 1. id로 DB에서 Entity를 호출
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 가져온 데이터를 모델에 등록
        model.addAttribute("article", articleEntity);

        // 3. 페이지 반환
        return "articles/show";
    }

    // 모든 게시글 조회 메소드
    @GetMapping("/articles")
    public String main(Model model){
        // 1. 모든 article을 가져온다
        List<Article> articleEntityList = articleRepository.findAll();

        // 2. 가져온 article 묶음을 뷰로 전달
        model.addAttribute("articleList", articleEntityList);

        // 3. 페이지 반환
        return "articles/main";
    }

    // 게시글 수정 메소드 #1
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 호출
        Article target = articleRepository.findById(id).orElse(null);

        // 모델에 데이터 등록
        model.addAttribute("article", target);

        // 페이지 반환
        return "articles/edit";
    }

    // 게시글 수정 메소드 #2 (페이지 form에서 받은 데이터)
    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        // Dto를 Entity로 변환
        Article article = form.toEntity();
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