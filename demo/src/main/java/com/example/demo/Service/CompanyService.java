package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Meta;
import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.Util.Error.IDInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        Company newCompany = company;
        newCompany.setName(company.getName());
        newCompany.setDescription(company.getDescription());
        newCompany.setAddress(company.getAddress());
        newCompany.setLogo(company.getLogo());
        newCompany.setCreatedAt(company.getCreatedAt());
        newCompany.setCreatedBy(company.getCreatedBy());
        this.companyRepository.save(newCompany);
        return newCompany;
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> fetchCompanyByID = this.companyRepository.findById(id);
        if (!fetchCompanyByID.isPresent()) {
            throw new IDInvalidException("please check your ID");
        }
        return fetchCompanyByID.get();
    }

    public ResultPaginationDTO fetchListCompanies(Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageCompany.getNumber());
        meta.setPageSize(pageCompany.getSize());
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
        currentCompany.setCreatedAt(updateCompany.getUpdatedAt());
        currentCompany.setCreatedBy(updateCompany.getUpdatedBy());
        this.companyRepository.save(currentCompany);
        return currentCompany;
    }

    public void deleteCompanyByID(long id) {
        Optional<Company> deleteCompany = this.companyRepository.findById(id);
        if (!deleteCompany.isPresent()) {
            throw new IDInvalidException("No ID exists: " + id);
        }
        this.companyRepository.deleteById(id);
    }
}
