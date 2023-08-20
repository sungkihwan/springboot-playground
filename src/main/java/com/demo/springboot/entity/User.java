package com.demo.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo.springboot.entity.base.Auditable;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "sm_user",
        indexes = @Index(columnList = "refreshToken", name = "refresh_token_index")
)
@EqualsAndHashCode(callSuper = false)
public class User extends Auditable {

    @Id
    @Column(updatable = false, nullable = false)
    private String email;

    @JoinColumn(name = "email")
    @OneToMany
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    @Column(nullable = true)
    private String dept;

    @Column(nullable = false)
    private String userNm;

    @Column(nullable = false)
    @JsonIgnore
    private String passwd;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private int loginCount = 0;
    
    @JsonIgnore
    @Column(unique = true)
    private String refreshToken;

    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private Boolean accessWeb = true;

    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private Boolean accessMobile = true;

    @Column(columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean activated = true;

    @Builder.Default
    @Column(nullable = true, columnDefinition = "bigint default 0")
    private Long orgIdx = 0L;

    @Column(nullable = true, columnDefinition = "varchar(255) default '14315311.258011518'")
    private String geoX;

    @Column(nullable = true, columnDefinition = "varchar(255) default '4280756.842524886'")
    private String geoY;

}
