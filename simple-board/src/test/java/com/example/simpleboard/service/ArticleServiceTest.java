package com.example.simpleboard.service;

import com.example.simpleboard.dto.ArticleDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest     // 스프링부트 연동 테스트
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;
    Member aM = new Member("naver_3LFCjuO0TCZq09CXWJzhTEuVVuZs4KFtxz34AsrCXN4", "남동곤", "sns1178@naver.com", "ROLE_USER", "");

    @Test
    void indexAll() {
        // 준비
        Article a = new Article(1L, "가가가가", "ㄱㄱㄱㄱ", aM);
        Article b = new Article(2L, "나나나나", "ㄴㄴㄴㄴ", aM);
        Article c = new Article(3L, "다다다다", "ㄷㄷㄷㄷ", aM);

        // 예상
        List<Article> expected = Arrays.asList(a, b, c);

        // 실제
        List<Article> articles = articleService.indexAll();

        // 비교
        assertEquals(expected.toString(), articles.toString());
    }

    @Test
    void index_성공____존재하는_id() {
        // 준비
        Long id = 1L;

        // 예상
        Article expected = new Article(id, "가가가가", "ㄱㄱㄱㄱ", aM);

        // 실제
        Article article = articleService.index(id);

        // 비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    void index_실패____존재하지_않는_id() {
        // 준비
        Long id = -1L;

        // 예상
        Article expected = null;

        // 실제
        Article article = articleService.index(id);

        // 비교
        assertEquals(expected, article);
    }

    @Test
    void create_성공____title_content_입력() {
        // 준비
        for(int i = 1; i <= 300; i++){
            String title = String.format("%d번째 제목 테스트", i);
            String content = String.format("%d번째 내용 테스트", i);
            ArticleDto dto = new ArticleDto(null, title, content, "naver_3LFCjuO0TCZq09CXWJzhTEuVVuZs4KFtxz34AsrCXN4");
            articleService.create(dto);
        }

        // 예상
//        Article expected = new Article(4L, title, content);

        // 실제

        // 비교
//        assertEquals(expected.toString(), article.toString());
    }}

//    @Test
//    @Transactional
//    void create_실패____id가_포함된_dto() {
//        // 준비
//        Long id = 4L;
//        String title = "라라라라";
//        String content = "ㄹㄹㄹㄹ";
//        ArticleDto dto = new ArticleDto(id, title, content);
//
//        // 예상
//        Article expected = null;
//
//        // 실제
//        ArticleDto article = articleService.create(dto);
//
//        // 비교
//        assertEquals(expected, article);
//    }

//    @Test
//    @Transactional
//    void update_성공____존재하는_id와_title_content의_dto() {
//        // 준비
//        Long id = 3L;
//        String title = "라라라라";
//        String content = "ㄹㄹㄹㄹ";
//        ArticleDto dto = new ArticleDto(id, title, content);
//
//        // 예상
//        Article expected = new Article(id, "라라라라", "ㄹㄹㄹㄹ");
//
//        // 실제
//        ArticleDto article = articleService.update(id, dto);
//
//        // 비교
//        assertEquals(expected.toString(), article.toString());
//    }
//
//    @Test
//    @Transactional
//    void update_성공____존재하는_id와_title의_dto() {
//        // 준비
//        Long id = 3L;
//        String title = "라라라라";
//        String content = null;
//        ArticleDto dto = new ArticleDto(id, title, content);
//
//        // 예상
//        Article expected = new Article(id, "라라라라", "ㄷㄷㄷㄷ");
//
//        // 실제
//        ArticleDto article = articleService.update(id, dto);
//
//        // 비교
//        assertEquals(expected.toString(), article.toString());
//    }
//
//    @Test
//    @Transactional
//    void update_실패____존재하지_않는_id의_dto() {
//        // 준비
//        Long id = 4L;
//        String title = "라라라라";
//        String content = "ㄹㄹㄹㄹ";
//        ArticleDto dto = new ArticleDto(id, title, content);
//
//        // 예상
//        Article expected = null;
//
//        // 실제
//        ArticleDto article = articleService.update(id, dto);
//
//        // 비교
//        assertEquals(expected, article);
//    }
//
//    @Test
//    @Transactional
//    void update_실패____id만_있는_dto() {
//        // 준비
//        Long id = 3L;
//        String title = null;
//        String content = null;
//        ArticleDto dto = new ArticleDto(id, title, content);
//
//        // 예상
//        Article expected = null;
//
//        // 실제
//        ArticleDto article = articleService.update(id, dto);
//
//        // 비교
//        assertEquals(expected, article);
//    }
//
//    @Test
//    @Transactional
//    void delete_성공____존재하는_id() {
//        // 준비
//        Long id = 3L;
//        String title = "다다다다";
//        String content = "ㄷㄷㄷㄷ";
//
//        // 예상
//        Article expected = new Article(id, title, content);
//
//        // 실제
//        ArticleDto article = articleService.delete(id);
//
//        // 비교
//        assertEquals(expected.toString(), article.toString());
//    }
//
//    @Test
//    @Transactional
//    void delete_실패____존재하지_않는_id() {
//        // 준비
//        Long id = 4L;
//
//        // 예상
//        Article expected = null;
//
//        // 실제
//        ArticleDto article = articleService.delete(id);
//
//        // 비교
//        assertEquals(expected, article);
//    }
//}