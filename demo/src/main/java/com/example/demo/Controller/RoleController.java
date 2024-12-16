package com.example.demo.Controller;

import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.Role;
import com.example.demo.Service.RoleService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    public RoleService roleService;
    public ResponseUtil responseUtil;

    public RoleController(RoleService roleService, ResponseUtil responseUtil) {
        this.roleService = roleService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/roles")
    public ResponseEntity<RestResponse<Object>> createRole(@Valid @RequestBody Role postManRole){
        return this.responseUtil.buildCreateResponse("create a new role successfully", this.roleService.createRole(postManRole));
    }

    @PutMapping("/roles")
    public ResponseEntity<RestResponse<Object>> updateRole(@Valid @RequestBody Role postManRole){
        return this.responseUtil.buildSuccessResponse("update the role successfully", this.roleService.updateRole(postManRole));
    }

    @GetMapping("/roles")
    public ResponseEntity<RestResponse<Object>> fetchListRoles(@Filter Specification<Role> specification, Pageable pageable){
        return this.responseUtil.buildSuccessResponse("fetch list roles successfully", this.roleService.fetchListRoles(specification, pageable));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<RestResponse<Object>> deleteRole(@PathVariable("id") long id){
        this.roleService.deleteRole(id);
        return this.responseUtil.buildSuccessResponse("delete the role successfully", null);
    }
}
