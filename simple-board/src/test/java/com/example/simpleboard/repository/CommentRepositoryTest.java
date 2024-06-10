//package com.example.simpleboard.repository;
//
//import com.example.simpleboard.entity.Article;
//import com.example.simpleboard.entity.Comment;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest    // JPA와 연동한 테스트!
//class CommentRepositoryTest {
//    @Autowired
//    CommentRepository commentRepository;
//
//    @Test
//    @DisplayName("특정 게시글의 모든 댓글 조회")
//    void findByArticleId() {
//        // case 1: 4번 게시글의 모든 댓글 조회
//        {
//            // 준비
//            Long articleId = 4L;
//            Article article = new Article(4L, "영화추천", "댓글ㄱ");
//
//            // 예상
//
//            Comment a = new Comment(1L, article, "PARK", "차우");
//            Comment b = new Comment(2L, article, "NAM", "파묘");
//            Comment c = new Comment(3L, article, "LEE", "곡성");
//            List<Comment> expected = Arrays.asList(a, b, c);
//
//            // 실제
//            List<Comment> comments = commentRepository.findByArticleId(articleId);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 2: 1번 게시글의 모든 댓글 조회
//        {
//            // 준비
//            Long articleId = 1L;
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByArticleId(articleId);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 3: 9번 게시글의 모든 댓글 조회
//        {
//            // 준비
//            Long articleId = 9L;
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByArticleId(articleId);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 4: 9999번 게시글의 모든 댓글 조회
//        {
//            // 준비
//            Long articleId = 9999L;
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByArticleId(articleId);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 3: -1번 게시글의 모든 댓글 조회
//        {
//            // 준비
//            Long articleId = -1L;
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByArticleId(articleId);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//    }
//
//    @Test
//    @DisplayName("특정 닉네임의 모든 댓글 조회")
//    void findByNickname() {
//        // case 1: "PARK"의 모든 댓글 조회
//        {
//            // 준비
//            String nickname = "PARK";
//            Comment a = new Comment(1L, new Article(4L, "영화추천", "댓글ㄱ"), "PARK", "차우");
//            Comment b = new Comment(4L, new Article(5L, "음식추천", "댓글ㄱㄱ"), "PARK", "치킨");
//            Comment c = new Comment(7L, new Article(6L, "게임추천", "댓글ㄱㄱㄱ"), "PARK", "롤");
//
//            // 예상
//            List<Comment> expected = Arrays.asList(a, b, c);
//
//            // 실제
//            List<Comment> comments = commentRepository.findByNickname(nickname);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 2: "NAM"의 모든 댓글 조회
//        {
//            // 준비
//            String nickname = "NAM";
//            Comment a = new Comment(2L, new Article(4L, "영화추천", "댓글ㄱ"), "NAM", "파묘");
//            Comment b = new Comment(5L, new Article(5L, "음식추천", "댓글ㄱㄱ"), "NAM", "피자");
//            Comment c = new Comment(8L, new Article(6L, "게임추천", "댓글ㄱㄱㄱ"), "NAM", "피파");
//
//            // 예상
//            List<Comment> expected = Arrays.asList(a, b, c);
//
//            // 실제
//            List<Comment> comments = commentRepository.findByNickname(nickname);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 3: null의 모든 댓글 조회
//        {
//            // 준비
//            String nickname = null;
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByNickname(nickname);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 4: " "의 모든 댓글 조회
//        {
//            // 준비
//            String nickname = " ";
//
//            // 예상
//            List<Comment> expected = Arrays.asList();
//
//            // 실제
//            List<Comment> comments = commentRepository.findByNickname(nickname);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//
//        // case 5: "A"가 포함된 모든 댓글 조회
//        {
//            // 준비
//            String nickname = "A";
//            Comment a = new Comment(1L, new Article(4L, "영화추천", "댓글ㄱ"), "PARK", "차우");
//            Comment b = new Comment(2L, new Article(4L, "영화추천", "댓글ㄱ"), "NAM", "파묘");
//
//            Comment c = new Comment(4L, new Article(5L, "음식추천", "댓글ㄱㄱ"), "PARK", "치킨");
//            Comment d = new Comment(5L, new Article(5L, "음식추천", "댓글ㄱㄱ"), "NAM", "피자");
//
//            Comment e = new Comment(7L, new Article(6L, "게임추천", "댓글ㄱㄱㄱ"), "PARK", "롤");
//            Comment f = new Comment(8L, new Article(6L, "게임추천", "댓글ㄱㄱㄱ"), "NAM", "피파");
//
//            // 예상
//            List<Comment> expected = Arrays.asList(a, b, c, d, e, f);
//
//            // 실제
//            List<Comment> comments = commentRepository.findByNickname(nickname);
//
//            // 비교
//            assertEquals(expected.toString(), comments.toString());
//        }
//    }
//}