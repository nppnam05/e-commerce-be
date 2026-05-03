package com.e_commerce.e_commerce_api.config.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.e_commerce.e_commerce_api.constant.SystemEntity;

import java.util.Optional;

@Component
public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 1. Lấy đối tượng Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Kiểm tra xem có ai đăng nhập không, hoặc có phải là người dùng ẩn danh
        // (Anonymous) không
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(SystemEntity.SYSTEM.toString());
        }

        // 3. Lấy Principal và trả về Email (Username)
        // Lưu ý: Principal thường là đối tượng UserDetails đã ném vào Filter
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return Optional.ofNullable(userDetails.getUsername());
        }

        return Optional.ofNullable(principal.toString());
    }
}