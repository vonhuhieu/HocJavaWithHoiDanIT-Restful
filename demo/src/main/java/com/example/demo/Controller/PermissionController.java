package com.example.demo.Controller;

import com.example.demo.Domain.Permission;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.PermissionService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private PermissionService permissionService;
    private ResponseUtil responseUtil;

    public PermissionController(PermissionService permissionService, ResponseUtil responseUtil) {
        this.permissionService = permissionService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/permissions")
    public ResponseEntity<RestResponse<Object>> createPermission(@Valid @RequestBody Permission postManPermission){
        return this.responseUtil.buildCreateResponse("Create a new permission successfully", this.permissionService.createPermission(postManPermission));
    }

    @PutMapping("/permissions")
    public ResponseEntity<RestResponse<Object>> updatePermission(@Valid @RequestBody Permission postManPermission){
        return this.responseUtil.buildSuccessResponse("Update the permission successfully", this.permissionService.updatePermission(postManPermission));
    }

    @GetMapping("/permissions")
    public ResponseEntity<RestResponse<Object>> fetchListPermissions(@Filter Specification<Permission> specification, Pageable pageable) {
        return this.responseUtil.buildSuccessResponse("Get list skills successfully", this.permissionService.fetchListPermissions(specification, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<RestResponse<Object>> deletePermissions(@PathVariable("id") long id){
        this.permissionService.deletePermission(id);
        return this.responseUtil.buildSuccessResponse("Delete the permission successfully", null);
    }
}
