package com.kspia.sscmservice.repository;

import com.kspia.sscmservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("UPDATE SmUser u SET u.refreshToken = :refreshToken WHERE u.email = :email")
    void updateRefreshToken(@Param("email") String email, @Param("refreshToken") String refreshToken);

//    @Modifying
//    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
//    // 해당 메서드는 새로운 트랜잭션에서 실행되므로, 해당 트랜잭션에서 변경된 내용은 상위 트랜잭션의 롤백 대상이 아니게 되는 옵션
//    @Query("UPDATE SmUser u SET u.loginCount = u.loginCount +1 WHERE u.email = :email")
//    void updateLoginFailCount(@Param("email") String email);
}
