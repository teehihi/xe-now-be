package com.rental.controller;

import com.rental.dto.AuthResponseDTO;
import com.rental.dto.LoginRequest;
import com.rental.entity.Role;
import com.rental.entity.User;
import com.rental.repository.RoleRepository;
import com.rental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Check if email or username already exists
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Email đã được sử dụng");
            }
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("Username đã được sử dụng");
            }
            
            // Get CUSTOMER role
            Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Role CUSTOMER không tồn tại"));
            
            // Setup user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(customerRole);
            user.setStatus(User.Status.Active);
            
            // Save user only
            userRepository.save(user);
            
            return ResponseEntity.ok("Đăng ký thành công! Vui lòng đăng nhập và xác thực danh tính.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đăng ký thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt: " + loginRequest.getUsername());
            
            // Find user by username or email
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .or(() -> userRepository.findByEmail(loginRequest.getUsername()))
                    .orElse(null);
            
            if (user == null) {
                System.out.println("User not found: " + loginRequest.getUsername());
                return ResponseEntity.ok(new AuthResponseDTO(
                    "Sai tài khoản hoặc mật khẩu",
                    null,
                    null,
                    false
                ));
            }
            
            System.out.println("User found: " + user.getUsername());
            System.out.println("Stored password hash: " + user.getPassword());
            System.out.println("Input password: " + loginRequest.getPassword());
            
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            System.out.println("Password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                return ResponseEntity.ok(new AuthResponseDTO(
                    "Sai tài khoản hoặc mật khẩu",
                    null,
                    null,
                    false
                ));
            }
            
            // Check if user is active
            if (user.getStatus() != User.Status.Active) {
                return ResponseEntity.ok(new AuthResponseDTO(
                    "Tài khoản đã bị khóa",
                    null,
                    null,
                    false
                ));
            }
            
            System.out.println("Login successful for: " + user.getUsername());
            return ResponseEntity.ok(new AuthResponseDTO(
                "Đăng nhập thành công",
                user.getUsername(),
                user.getRole().getRoleName(),
                true
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new AuthResponseDTO(
                "Đăng nhập thất bại: " + e.getMessage(),
                null,
                null,
                false
            ));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }
        
        // Get user from database
        String username = authentication.getName();
        var user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(404).body("Không tìm thấy thông tin người dùng");
        }
        
        // Return user info
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("user", new java.util.HashMap<String, Object>() {{
                put("userId", user.getUserId());
                put("username", user.getUsername());
                put("fullName", user.getFullName());
                put("email", user.getEmail());
                put("phone", user.getPhone());
                put("dateOfBirth", user.getDateOfBirth());
                put("role", user.getRole().getRoleName());
                put("status", user.getStatus().toString());
            }});
            put("authenticated", true);
        }});
    }
}
