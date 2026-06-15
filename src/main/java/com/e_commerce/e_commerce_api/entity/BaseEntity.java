package com.e_commerce.e_commerce_api.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.e_commerce.e_commerce_api.constant.StatusEntity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // Đánh dấu đây là lớp cha, không tạo bảng riêng
@EntityListeners(AuditingEntityListener.class) // Kích hoạt lắng nghe sự kiện Audit
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "CreatedBy", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "CreatedOn", updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedBy
    @Column(name = "ModifiedBy")
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "ModifiedOn")
    private LocalDateTime modifiedOn;

    @Column(length = 5, name = "Status")
    protected String status;

    @PrePersist
    public void prePersist() {
        if (this.status == null)
            this.status = StatusEntity.ACT.toString();
    }

    public void Delete() {
        this.status = StatusEntity.DEL.toString();
    }
}
