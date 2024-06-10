package com.example.simpleboard.repository;

import com.example.simpleboard.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT *" +
                    "FROM Article" +
                    "WHERE title LIKE %:title%", nativeQuery = true)
    List<Article> findByTitle(@Param("title")String title);

    Page<Article> findByTitleContaining(String title, Pageable pageable);
}
