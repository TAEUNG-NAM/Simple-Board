package com.example.simpleboard.repository;

import com.example.simpleboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    Boolean existsByAccess(String access);

    @Modifying
    @Transactional
    @Query(value = "update Member set access = null where access = :access", nativeQuery = true)
    void deleteByAccess(@Param("access")String access);
}
