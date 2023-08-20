package com.kspia.sscmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sm_role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @JoinColumn(name = "role_cd")
    @OneToMany
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    @Id
    @Column(name = "role_cd", nullable = false)
    private String roleCd;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activated = true;

    @Column(nullable = false)
    private String roleNm;

}

