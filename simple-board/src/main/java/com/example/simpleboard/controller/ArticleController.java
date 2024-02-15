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

    // 게시글 수정 메소드
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 호출
        Article target = articleRepository.findById(id).orElse(null);

        // 모델에 데이터 등록
        model.addAttribute("article", target);

        // 페이지 반환
        return "articles/edit";
    }
}
