package com.demo.springboot.repository;

import com.demo.springboot.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM sm_user_role WHERE email = :email", nativeQuery = true)
    void deleteByEmail(@Param("email") String email);

}
