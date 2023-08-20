package com.demo.springboot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class UserRoleKey implements Serializable {
    private String email;
    private String roleCd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleKey that = (UserRoleKey) o;
        return Objects.equals(email, that.email) && Objects.equals(roleCd, that.roleCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, roleCd);
    }
}


