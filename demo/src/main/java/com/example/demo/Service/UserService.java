package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Response.Company.CompanyDTO;
import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.DTO.Response.Role.RoleDTO;
import com.example.demo.Domain.DTO.Response.User.UserFormatDataResponseDTO;
import com.example.demo.Domain.Role;
import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public UserFormatDataResponseDTO handleCreateUser(User user) {
        boolean checkEmailExists = this.userRepository.existsByEmail(user.getEmail());
        if (checkEmailExists){
            throw new ExistsByData("Email is already used by other person");
        }
        Company company = this.companyService.fetchCompanyById(user.getCompany().getId());
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        Role role = this.roleService.fetchRoleById(user.getRole().getId());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hashPassword);
        newUser.setAddress(user.getAddress());
        newUser.setAge(user.getAge());
        newUser.setGender(user.getGender());
        newUser.setCompany(company);
        newUser.setRole(role);
        this.userRepository.save(newUser);
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(newUser.getId());
        newUserFormatDataResponseDTO.setEmail(newUser.getEmail());
        newUserFormatDataResponseDTO.setName(newUser.getName());
        newUserFormatDataResponseDTO.setGender(newUser.getGender());
        newUserFormatDataResponseDTO.setAddress(newUser.getAddress());
        newUserFormatDataResponseDTO.setAge(newUser.getAge());
        newUserFormatDataResponseDTO.setCreatedBy(newUser.getCreatedBy());
        newUserFormatDataResponseDTO.setCreatedAt(newUser.getCreatedAt());
        newUserFormatDataResponseDTO.setCompanyDTO(companyDTO);
        newUserFormatDataResponseDTO.setRoleDTO(roleDTO);
        return newUserFormatDataResponseDTO;
    }

    public User getUserByID(long id) {
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        User fetchUser = fetchUserByID.get();
        return fetchUser;
    }

    public UserFormatDataResponseDTO fetchUserByID(long id){
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        User fetchUser = fetchUserByID.get();
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(fetchUser.getCompany().getId());
        companyDTO.setName(fetchUser.getCompany().getName());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(fetchUser.getRole().getId());
        roleDTO.setName(fetchUser.getRole().getName());
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(fetchUser.getId());
        newUserFormatDataResponseDTO.setEmail(fetchUser.getEmail());
        newUserFormatDataResponseDTO.setName(fetchUser.getName());
        newUserFormatDataResponseDTO.setGender(fetchUser.getGender());
        newUserFormatDataResponseDTO.setAddress(fetchUser.getAddress());
        newUserFormatDataResponseDTO.setAge(fetchUser.getAge());
        newUserFormatDataResponseDTO.setCreatedAt(fetchUser.getCreatedAt());
        newUserFormatDataResponseDTO.setCreatedBy(fetchUser.getCreatedBy());
        newUserFormatDataResponseDTO.setUpdatedBy(fetchUser.getUpdatedBy());
        newUserFormatDataResponseDTO.setUpdatedAt(fetchUser.getUpdatedAt());
        newUserFormatDataResponseDTO.setCompanyDTO(companyDTO);
        newUserFormatDataResponseDTO.setRoleDTO(roleDTO);
        return newUserFormatDataResponseDTO;
    }

    public void deleteUserByID(long id) {
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        this.userRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        result.setMeta(meta);
        List<UserFormatDataResponseDTO> listUsers = new ArrayList<>();
        for (User user : pageUser.getContent()){
            Company company = user.getCompany();
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(company.getId());
            companyDTO.setName(company.getName());
            Role role = user.getRole();
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
            newUserFormatDataResponseDTO.setId(user.getId());
            newUserFormatDataResponseDTO.setEmail(user.getEmail());
            newUserFormatDataResponseDTO.setName(user.getName());
            newUserFormatDataResponseDTO.setGender(user.getGender());
            newUserFormatDataResponseDTO.setAddress(user.getAddress());
            newUserFormatDataResponseDTO.setAge(user.getAge());
            newUserFormatDataResponseDTO.setCreatedAt(user.getCreatedAt());
            newUserFormatDataResponseDTO.setCreatedBy(user.getCreatedBy());
            newUserFormatDataResponseDTO.setUpdatedBy(user.getUpdatedBy());
            newUserFormatDataResponseDTO.setUpdatedAt(user.getUpdatedAt());
            newUserFormatDataResponseDTO.setCompanyDTO(companyDTO);
            newUserFormatDataResponseDTO.setRoleDTO(roleDTO);
            listUsers.add(newUserFormatDataResponseDTO);
        }
        result.setResult(listUsers);
        return result;
    }

    public UserFormatDataResponseDTO updateUser(User updateUser){
        User currentUser = this.getUserByID(updateUser.getId());
        if(currentUser == null){
            throw new IDInvalidException("no exists ID " + updateUser.getId());
        }
        Company company = this.companyService.fetchCompanyById(updateUser.getCompany().getId());
        Role role = this.roleService.fetchRoleById(updateUser.getRole().getId());
        currentUser.setName(updateUser.getName());
        currentUser.setGender(updateUser.getGender());
        currentUser.setAge(updateUser.getAge());
        currentUser.setAddress(updateUser.getAddress());
        currentUser.setCompany(company);
        currentUser.setRole(role);
        this.userRepository.save(currentUser);
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(currentUser.getId());
        newUserFormatDataResponseDTO.setEmail(currentUser.getEmail());
        newUserFormatDataResponseDTO.setName(currentUser.getName());
        newUserFormatDataResponseDTO.setGender(currentUser.getGender());
        newUserFormatDataResponseDTO.setAddress(currentUser.getAddress());
        newUserFormatDataResponseDTO.setAge(currentUser.getAge());
        newUserFormatDataResponseDTO.setCreatedAt(currentUser.getCreatedAt());
        newUserFormatDataResponseDTO.setCreatedBy(currentUser.getCreatedBy());
        newUserFormatDataResponseDTO.setUpdatedBy(currentUser.getUpdatedBy());
        newUserFormatDataResponseDTO.setUpdatedAt(currentUser.getUpdatedAt());
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        newUserFormatDataResponseDTO.setCompanyDTO(companyDTO);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        newUserFormatDataResponseDTO.setRoleDTO(roleDTO);
        return newUserFormatDataResponseDTO;
    }

    public User handleGetUserByUserName(String username){
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUserName(email);
        if (currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token,email);
    }
}
