package com.example.simpleboard.api;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.repository.ArticleRepository;
import com.example.simpleboard.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {
    @Autowired
    CommentService commentService;

    // 댓글 조회(GET)
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId){
        // 서비스 호출
        List<CommentDto> dtos = commentService.comments(articleId);

        // 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 댓글 생성(POST)
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, @RequestBody CommentDto dto){
        // 서비스 호출
        CommentDto created = commentService.create(articleId, dto);

        // 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    // 댓글 수정(PATCH)
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto dto){
        // 서비스 호출
        CommentDto updated = commentService.update(id, dto);

        // 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // 댓글 삭제(DELETE)
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id){
        // 서비스 호출
        CommentDto deleted = commentService.delete(id);

        // 결과 반환
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

}
