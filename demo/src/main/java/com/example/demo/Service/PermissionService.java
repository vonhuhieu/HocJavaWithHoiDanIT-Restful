package com.example.demo.Service;

import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.Permission;
import com.example.demo.Domain.Role;
import com.example.demo.Repository.PermissionRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission){
        boolean checkExistsByName = permissionRepository.existsByName(permission.getName());
        if (checkExistsByName){
            throw new ExistsByData("Name, Method already exist");
        }
        Permission newPermission = new Permission();
        newPermission.setName(permission.getName());
        newPermission.setApiPath(permission.getApiPath());
        newPermission.setMethod(permission.getMethod());
        newPermission.setModule(permission.getModule());
        this.permissionRepository.save(newPermission);
        return newPermission;
    }

    public Permission updatePermission(Permission permission){
        Optional<Permission> checkExistsByPermission = this.permissionRepository.findById(permission.getId());
        if (checkExistsByPermission.isEmpty()){
            throw new IDInvalidException("No exists permission whose id = " + permission.getId());
        }
        Permission currentPermission = checkExistsByPermission.get();
        boolean checkExistsByName = permissionRepository.existsByName(permission.getName());
        if (checkExistsByName && currentPermission.getId() != permission.getId()){
            throw new ExistsByData("Name, Method already exist");
        }
        currentPermission.setName(permission.getName());
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setModule(permission.getModule());
        this.permissionRepository.save(currentPermission);
        return currentPermission;
    }

    public ResultPaginationDTO fetchListPermissions(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());
        result.setMeta(meta);
        result.setResult(pagePermission.getContent());
        return result;
    }

    public void deletePermission(long id){
        Optional<Permission> fetchPermissionById = this.permissionRepository.findById(id);
        if (fetchPermissionById.isEmpty()){
            throw new IDInvalidException("No exists permission whose id = " + id);
        }
        List<Role> listRoles = fetchPermissionById.get().getRoles();
        // khi xoa skill luu y tim den list jobs co chua skill do va xoa di, chu khong phai xoa luon ca job
        for (Role role :listRoles){
            role.getPermissions().remove(fetchPermissionById.get());
        }
        this.permissionRepository.deleteById(fetchPermissionById.get().getId());
    }
}
