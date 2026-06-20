package com.e_commerce.e_commerce_api.entity;

import com.e_commerce.e_commerce_api.constant.StatusEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
}
