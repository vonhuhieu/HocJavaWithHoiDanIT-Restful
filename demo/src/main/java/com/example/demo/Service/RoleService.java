package com.example.demo.Service;

import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.Permission;
import com.example.demo.Domain.Role;
import com.example.demo.Repository.PermissionRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public RoleService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role){
        boolean checkExistsByName = this.permissionRepository.existsByName(role.getName());
        if (checkExistsByName){
            throw new ExistsByData("Role already exists");
        }
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setActive(role.isActive());
        newRole.setDescription(role.getDescription());
        List<Long> idPermissions = new ArrayList<>();
        for (Permission permission : role.getPermissions()){
            idPermissions.add(permission.getId());
        }
        List<Permission> listPermissions = this.permissionRepository.findByIdIn(idPermissions);
        newRole.setPermissions(listPermissions);
        this.roleRepository.save(newRole);
        return newRole;
    }

    public Role updateRole(Role role){
        Optional<Role> checkExistsByRole = this.roleRepository.findById(role.getId());
        if (checkExistsByRole.isEmpty()){
            throw new IDInvalidException("No exists role whose id = " + role.getId());
        }
        boolean checkExistsByName = this.roleRepository.existsByName(role.getName());
        if (checkExistsByName && checkExistsByRole.get().getId() != role.getId()){
            throw new ExistsByData("Role already exists");
        }
        Role currentRole = checkExistsByRole.get();
        currentRole.setName(role.getName());
        currentRole.setActive(role.isActive());
        currentRole.setDescription(role.getDescription());
        List<Long> idPermissions = new ArrayList<>();
        for (Permission permission : role.getPermissions()){
            idPermissions.add(permission.getId());
        }
        List<Permission> listPermissions = this.permissionRepository.findByIdIn(idPermissions);
        currentRole.setPermissions(listPermissions);
        this.roleRepository.save(currentRole);
        return currentRole;
    }

    public ResultPaginationDTO fetchListRoles(Specification<Role> specification, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageRole.getTotalPages());
        meta.setTotal(pageRole.getTotalElements());
        result.setMeta(meta);
        result.setResult(pageRole.getContent());
        return result;
    }

    public void deleteRole(long id){
        Optional<Role> checkExistsByRole = this.roleRepository.findById(id);
        if (checkExistsByRole.isEmpty()){
            throw new IDInvalidException("No exists role whose id = " + id);
        }
        this.roleRepository.deleteById(checkExistsByRole.get().getId());
    }
}
