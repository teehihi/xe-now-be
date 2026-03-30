package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.entity.Permission;
import com.rental.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissions() {
        return ResponseEntity.ok(ApiResponse.success(permissionService.findAll(), "Lấy danh sách quyền thành công"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(permissionService.findById(id), "Lấy thông tin quyền thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Không tìm thấy quyền với ID: " + id));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Permission>> createPermission(@RequestBody Permission permission) {
        try {
            Permission created = permissionService.create(permission);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(created, "Tạo quyền mới thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@PathVariable Integer id, @RequestBody Permission permissionDetails) {
        try {
            Permission updated = permissionService.update(id, permissionDetails);
            return ResponseEntity.ok(ApiResponse.success(updated, "Cập nhật quyền thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Không tìm thấy quyền với ID: " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePermission(@PathVariable Integer id) {
        try {
            permissionService.delete(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Xóa quyền thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Không tìm thấy quyền với ID: " + id));
        }
    }
}
