package com.demo.springboot.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable extends BaseEntity {
    @CreatedBy
    @JsonIgnore
    @Column(name="created_user")
    private String createdUser;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="created_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;

    @LastModifiedBy
    @JsonIgnore
    @Column(name="last_modified_user")
    private String lastModifiedUser;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name="last_modified_date")
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime lastModifiedDate;
}
