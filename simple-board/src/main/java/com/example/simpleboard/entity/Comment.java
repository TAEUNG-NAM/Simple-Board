package com.example.simpleboard.entity;

import com.example.simpleboard.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class  Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "member_id") // 특정 기능 이용하지 않을 시 생략 가능(ex nullable, name, updatable)
    private Member member;

    @Column
    private String body;

    public static Comment createComment(CommentDto dto, Article article, Member member) {
        // 예외 발생
        if(dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id를 지정할 수 없습니다.");
        if(article.getId() != dto.getArticleId())
            throw new IllegalArgumentException("댓글 생성 실패! id 불일치");

        // 엔티티 생성 및 반환
        return new Comment(dto.getId(), article, member, dto.getBody());
    }

    public Comment patch(CommentDto dto) {
        // 예외 발생
        if(this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! id 불일치.");

        // 객체 갱신
//        if(dto.getName() != null)
//            this.name = dto.getName();
        if(dto.getBody() != null)
            this.body = dto.getBody();

        return this;
    }
}
