package com.example.simpleboard.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

@MappedSuperclass   // jpa entity 클래스들이 BaseTimeEntity를 상속할 경우 부모클래스 필드도 컬럼으로 인식
@Getter
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능 부여
public class BaseTimeEntity {
    
    @CreatedDate
    @Column(name = "created", updatable = false)  // 수정 불가
    private LocalDateTime createdAt;    // 생성 시간

    @LastModifiedDate
    @Column(name = "modified")
    private LocalDateTime modifiedAt;   // 수정 시간
}
