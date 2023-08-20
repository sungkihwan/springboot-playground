package com.kspia.sscmservice.repository.querydsl;

import com.kspia.sscmservice.entity.Role;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.kspia.sscmservice.entity.QRole.role;

@Repository
@RequiredArgsConstructor
public class RoleCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<Role> findByRoleCdIn(List<String> roleCds) {
        return queryFactory.selectFrom(role)
                .where(role.roleCd.in(roleCds))
                .fetch();
    }

    public List<Role> selectroles(Pageable pageable) {
        BooleanExpression expression = role.isNotNull();

        return queryFactory.selectFrom(role)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public Long countroles() {
        return queryFactory.select(role.count())
                .from(role)
                .fetchFirst();
    }
}
