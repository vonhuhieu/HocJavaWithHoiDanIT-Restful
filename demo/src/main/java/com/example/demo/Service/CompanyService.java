package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Response.ResultPaginationDTO;
import com.example.demo.Domain.User;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.demo.Util.Error.IDInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company createCompany(Company company) {
        Company newCompany = company;
        newCompany.setName(company.getName());
        newCompany.setDescription(company.getDescription());
        newCompany.setAddress(company.getAddress());
        newCompany.setLogo(company.getLogo());
        this.companyRepository.save(newCompany);
        return newCompany;
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> fetchCompanyByID = this.companyRepository.findById(id);
        if (!fetchCompanyByID.isPresent()) {
            throw new IDInvalidException("please check ID's company");
        }
        return fetchCompanyByID.get();
    }

    public ResultPaginationDTO fetchListCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());
        result.setMeta(meta);
        result.setResult(pageCompany.getContent());
        return result;
    }

    public Company updateCompany(Company updateCompany) {
        Company currentCompany = this.fetchCompanyById(updateCompany.getId());
        currentCompany.setName(updateCompany.getName());
        currentCompany.setDescription(updateCompany.getDescription());
        currentCompany.setAddress(updateCompany.getAddress());
        currentCompany.setLogo(updateCompany.getLogo());
        this.companyRepository.save(currentCompany);
        return currentCompany;
    }

    public void deleteCompanyByID(long id) {
        Optional<Company> deleteCompany = this.companyRepository.findById(id);
        if (!deleteCompany.isPresent()) {
            throw new IDInvalidException("No ID exists: " + id);
        }
        //xóa users thuộc company trước, sau đó mơới xóa company
        List<User> listUsers = deleteCompany.get().getUsers();
        this.userRepository.deleteAll(listUsers);
        this.companyRepository.deleteById(id);
    }
}
