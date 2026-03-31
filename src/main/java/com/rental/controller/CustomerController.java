package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.dto.CustomerResponseDTO;
import com.rental.entity.Customer;
import com.rental.entity.User;
import com.rental.repository.CustomerRepository;
import com.rental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @GetMapping("/verify-status")
    public ResponseEntity<ApiResponse<VerificationStatusDTO>> checkVerificationStatus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Chưa đăng nhập"));
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        boolean isVerified = customerRepository.findById(user.getUserId()).isPresent();
        VerificationStatusDTO status = new VerificationStatusDTO(isVerified, user.getUserId());
        
        return ResponseEntity.ok(ApiResponse.success(status, "Kiểm tra trạng thái xác minh thành công"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(@PathVariable Integer userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Chưa đăng nhập"));
        }

        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng"));
        
        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .userId(customer.getUserId())
                .identityCard(customer.getIdentityCard())
                .identityCardIssueDate(customer.getIdentityCardIssueDate())
                .identityCardExpiry(customer.getIdentityCardExpiry())
                .address(customer.getAddress())
                .driverLicense(customer.getDriverLicense())
                .driverLicenseClass(customer.getDriverLicenseClass())
                .driverLicenseIssueDate(customer.getDriverLicenseIssueDate())
                .driverLicenseExpiry(customer.getDriverLicenseExpiry())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(dto, "Lấy thông tin khách hàng thành công"));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyIdentity(@RequestBody Customer customer, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Chưa đăng nhập"));
        }

        try {
            User user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

            if (customerRepository.findById(user.getUserId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Tài khoản đã được xác minh"));
            }

            customer.setUserId(user.getUserId());
            customer.setUser(user);
            customerRepository.save(customer);

            return ResponseEntity.ok(ApiResponse.success(null, "Xác minh danh tính thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Xác minh thất bại: " + e.getMessage()));
        }
    }

    public static class VerificationStatusDTO {
        public boolean verified;
        public Integer userId;

        public VerificationStatusDTO(boolean verified, Integer userId) {
            this.verified = verified;
            this.userId = userId;
        }
    }
}
