package com.demo.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sm_user_role")
@IdClass(UserRoleKey.class)
public class UserRole {

    @Id
    @Column(updatable = false, nullable = false)
    private String email;

    @Id
    @Column(name = "role_cd", nullable = false)
    private String roleCd;

}
