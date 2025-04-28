package com.stock.stockify.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// User 엔티티에 대한 JPA 레포지토리
// 회원 조회, 저장, 삭제 등의 기본 CRUD 제공
public interface UserRepository extends JpaRepository<User, Long> { // User 엔티티를 DB에 CRUD 하는 기본 기능 제공

    Optional<User> findByUsername(String username); // 아이디(username)로 회원을 조회하는 기능 (로그인에 사용 예정)

}
