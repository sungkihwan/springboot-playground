package com.kspia.sscmservice.repository.querydsl;

import com.kspia.sscmservice.entity.UserRole;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kspia.sscmservice.entity.QUserRole.userRole;


@Repository
@RequiredArgsConstructor
public class UserRoleCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<UserRole> findByEmail(String email) {
        return queryFactory.selectFrom(userRole)
                .where(userRole.email.eq(email))
                .fetch();
    }

    public void deleteByEmail(String email) {
        queryFactory.delete(userRole)
                .where(userRole.email.eq(email))
                .execute();
    }
}
