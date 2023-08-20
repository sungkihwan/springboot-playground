package com.demo.springboot.repository.querydsl;

import com.demo.springboot.dto.request.UserSearchDto;
import com.demo.springboot.entity.QUser;
import com.demo.springboot.entity.QUserRole;
import com.demo.springboot.entity.User;
import com.demo.springboot.dto.request.UserDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public long countusers(UserSearchDto userSearchDto) {
         List<String> emails = queryFactory
                .select(QUser.user.email)
                .from(QUser.user)
                .leftJoin(QUser.user.userRoles, QUserRole.userRole)
                .where(
                        emailLike(userSearchDto.getEmail()),
                        userNmEq(userSearchDto.getName()),
                        roleCdEq(userSearchDto.getRoleCode()),
                        QUser.user.deleted.eq(false)
                )
                .distinct()
                .fetch();

        return emails.size();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateActivatedStatus(String email, boolean activated) {
        queryFactory.update(QUser.user)
                .set(QUser.user.activated, activated)
                .where(QUser.user.email.eq(email))
                .execute();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateLoginFailCount(String email) {
        queryFactory.update(QUser.user)
                .set(QUser.user.loginCount, QUser.user.loginCount.add(1))
                .where(QUser.user.email.eq(email))
                .execute();
    }

    @Transactional
    public Boolean resetLoginCount(String email) {
        return queryFactory.update(QUser.user)
                .set(QUser.user.loginCount, 0)
                .set(QUser.user.activated, true)
                .where(QUser.user.email.eq(email))
                .execute() > 0;
    }

    public User findByEmail(String email) {
        return queryFactory.select(QUser.user)
                .from(QUser.user)
                .leftJoin(QUser.user.userRoles, QUserRole.userRole).fetchJoin()
                .where(QUser.user.email.eq(email))
                .fetchFirst();
    }

    public Boolean updateuser(UserDto userDto) {
        return queryFactory.update(QUser.user)
                .set(QUser.user.userNm, userDto.getName())
                .set(QUser.user.geoX, userDto.getGeoX())
                .set(QUser.user.geoY, userDto.getGeoY())
                .set(QUser.user.orgIdx, userDto.getOrgIdx())
                .set(QUser.user.activated, userDto.getActivated())
                .where(QUser.user.email.eq(userDto.getEmail()))
                .execute() > 0;
    }

    public boolean existsByEmail(String email) {
        return queryFactory.selectOne()
                .from(QUser.user)
                .where(
                        QUser.user.email.eq(email),
                        QUser.user.deleted.eq(false)
                )
                .fetchFirst() != null;
    }

    public void delete(String email) {
        queryFactory.update(QUser.user)
                .set(QUser.user.deleted, true)
                .where(QUser.user.email.eq(email))
                .execute();
    }

    // == 구문
    private BooleanExpression emailEq(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return QUser.user.email.eq(email);
    }

    // like 구문
    private BooleanExpression emailLike(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return QUser.user.email.like("%" + email + "%");
    }

    private BooleanExpression userNmLike(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return QUser.user.userNm.like("%" + name + "%");
    }

    private BooleanExpression userNmEq(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return QUser.user.userNm.eq(name);
    }

    private BooleanExpression deptEq(String department) {
        if (StringUtils.isEmpty(department)) {
            return null;
        }
        return QUser.user.dept.eq(department);
    }

    private BooleanExpression roleCdEq(String roleCode) {
        if (StringUtils.isEmpty(roleCode)) {
            return null;
        }
        return QUser.user.userRoles.any().roleCd.eq(roleCode);
    }

    private BooleanExpression activatedEq(String activated) {
        if (StringUtils.isEmpty(activated)) {
            return null;
        }
        boolean isActive = "true".equals(activated);
        return QUser.user.activated.eq(isActive);
    }

}
