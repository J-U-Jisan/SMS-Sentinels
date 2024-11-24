package com.example.app.smssentinels.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Nullable
    protected LocalDateTime updatedAt;
}
