package com.kspia.sscmservice.repository.querydsl;

import com.kspia.sscmservice.dto.request.UserDto;
import com.kspia.sscmservice.dto.request.UserSearchDto;
import com.kspia.sscmservice.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.kspia.sscmservice.entity.QUser.user;
import static com.kspia.sscmservice.entity.QUserRole.userRole;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public long countusers(UserSearchDto userSearchDto) {
         List<String> emails = queryFactory
                .select(user.email)
                .from(user)
                .leftJoin(user.userRoles, userRole)
                .where(
                        emailLike(userSearchDto.getEmail()),
                        userNmEq(userSearchDto.getName()),
                        roleCdEq(userSearchDto.getRoleCode()),
                        user.deleted.eq(false)
                )
                .distinct()
                .fetch();

        return emails.size();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateActivatedStatus(String email, boolean activated) {
        queryFactory.update(user)
                .set(user.activated, activated)
                .where(user.email.eq(email))
                .execute();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateLoginFailCount(String email) {
        queryFactory.update(user)
                .set(user.loginCount, user.loginCount.add(1))
                .where(user.email.eq(email))
                .execute();
    }

    @Transactional
    public Boolean resetLoginCount(String email) {
        return queryFactory.update(user)
                .set(user.loginCount, 0)
                .set(user.activated, true)
                .where(user.email.eq(email))
                .execute() > 0;
    }

    public User findByEmail(String email) {
        return queryFactory.select(user)
                .from(user)
                .leftJoin(user.userRoles, userRole).fetchJoin()
                .where(user.email.eq(email))
                .fetchFirst();
    }

    public Boolean updateuser(UserDto userDto) {
        return queryFactory.update(user)
                .set(user.userNm, userDto.getName())
                .set(user.geoX, userDto.getGeoX())
                .set(user.geoY, userDto.getGeoY())
                .set(user.orgIdx, userDto.getOrgIdx())
                .set(user.activated, userDto.getActivated())
                .where(user.email.eq(userDto.getEmail()))
                .execute() > 0;
    }

    public boolean existsByEmail(String email) {
        return queryFactory.selectOne()
                .from(user)
                .where(
                        user.email.eq(email),
                        user.deleted.eq(false)
                )
                .fetchFirst() != null;
    }

    public void delete(String email) {
        queryFactory.update(user)
                .set(user.deleted, true)
                .where(user.email.eq(email))
                .execute();
    }

    // == 구문
    private BooleanExpression emailEq(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return user.email.eq(email);
    }

    // like 구문
    private BooleanExpression emailLike(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return user.email.like("%" + email + "%");
    }

    private BooleanExpression userNmLike(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return user.userNm.like("%" + name + "%");
    }

    private BooleanExpression userNmEq(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return user.userNm.eq(name);
    }

    private BooleanExpression deptEq(String department) {
        if (StringUtils.isEmpty(department)) {
            return null;
        }
        return user.dept.eq(department);
    }

    private BooleanExpression roleCdEq(String roleCode) {
        if (StringUtils.isEmpty(roleCode)) {
            return null;
        }
        return user.userRoles.any().roleCd.eq(roleCode);
    }

    private BooleanExpression activatedEq(String activated) {
        if (StringUtils.isEmpty(activated)) {
            return null;
        }
        boolean isActive = "true".equals(activated);
        return user.activated.eq(isActive);
    }

}
