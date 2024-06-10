package com.example.simpleboard.api;

import com.example.simpleboard.dto.ArticleDto;
import com.example.simpleboard.entity.Article;
import com.example.simpleboard.service.ArticleService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ArticleApiController {

    @Autowired
    private ArticleService articleService;

    // GET
    @GetMapping("/api/articles")
    public List<Article> indexAll(){
        return articleService.indexAll();
    }

    // GET
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> index(@PathVariable Long id){
        Article article = articleService.index(id);
        return article != null ?
                ResponseEntity.status(HttpStatus.OK).body(article) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // POST
    @PostMapping("/api/articles")
    public ResponseEntity<ArticleDto> create(@RequestBody ArticleDto dto){
        ArticleDto created = articleService.create(dto);
        if(created != null){
            log.info(created.toString());
            return ResponseEntity.status(HttpStatus.OK).body(created);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> update(@PathVariable Long id, @RequestBody ArticleDto dto){
        ArticleDto updated = articleService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> delete(@PathVariable Long id){
        ArticleDto deleted = articleService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.OK).body(deleted) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
