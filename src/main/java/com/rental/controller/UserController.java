package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.entity.User;
import com.rental.exception.DuplicateResourceException;
import com.rental.exception.ResourceNotFoundException;
import com.rental.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController

@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(users, "Lấy danh sách người dùng thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với mã: " + id));
        return ResponseEntity.ok(ApiResponse.success(user, "Tìm thấy người dùng"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email đã tồn tại: " + user.getEmail());
        }

        User saved = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getUserId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.created(saved, "Tạo người dùng mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Integer id, @Valid @RequestBody User userDetails) {
        User updated = userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFullName(userDetails.getFullName());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setPhone(userDetails.getPhone());
                    existingUser.setAddress(userDetails.getAddress());
                    existingUser.setGender(userDetails.getGender());
                    existingUser.setDateOfBirth(userDetails.getDateOfBirth());
                    existingUser.setStatus(userDetails.getStatus());
                    
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng để cập nhật với mã: " + id));
        
        return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật thông tin người dùng thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng để xóa với mã: " + id));
        
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}

