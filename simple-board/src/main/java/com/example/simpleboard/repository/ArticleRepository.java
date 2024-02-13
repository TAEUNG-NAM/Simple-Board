package com.example.simpleboard.repository;

import com.example.simpleboard.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
