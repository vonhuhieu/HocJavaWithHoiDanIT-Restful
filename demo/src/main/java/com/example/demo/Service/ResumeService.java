package com.example.demo.Service;

import com.example.demo.Domain.*;

import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.DTO.Response.Resume.*;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.ResumeRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Util.Error.IDInvalidException;
import com.example.demo.Util.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private ResumeRepository resumeRepository;
    private JobRepository jobRepository;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;

    public ResumeService(ResumeRepository resumeRepository, JobRepository jobRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public ResumeDTO createResume(Resume resume){
        Optional<Job> checkJobExists = this.jobRepository.findById(resume.getJob().getId());
        if (checkJobExists.isEmpty()){
            throw new IDInvalidException("No exists job whose id = " + resume.getJob().getId());
        }
        boolean checkEmailExists = this.userRepository.existsByEmail(resume.getEmail());
        if (!checkEmailExists){
            throw new IDInvalidException("No exists email. Please create a new account to continue");
        }
        Resume newResume = new Resume();
        newResume.setEmail(resume.getEmail());
        newResume.setUrl(resume.getUrl());
        newResume.setStatus(resume.getStatus());
        newResume.setUser(resume.getUser());
        newResume.setJob(resume.getJob());
        this.resumeRepository.save(newResume);
        ResumeDTO resumeDTO = new ResumeDTO();
        resumeDTO.setId(newResume.getId());
        resumeDTO.setCreatedAt(newResume.getCreatedAt());
        resumeDTO.setCreatedBy(newResume.getCreatedBy());
        return resumeDTO;
    }

    public ResumeUpdateDTO updateResume(Resume resume){
        Optional<Resume> checkResumeExists = this.resumeRepository.findById(resume.getId());
        if (checkResumeExists.isEmpty()){
            throw new IDInvalidException("No exists Resume whose id = " + resume.getId());
        }
        Resume currentResumt = checkResumeExists.get();
        currentResumt.setStatus(resume.getStatus());
        this.resumeRepository.save(currentResumt);
        ResumeUpdateDTO resumeUpdateDTO = new ResumeUpdateDTO();
        resumeUpdateDTO.setUpdatedAt(currentResumt.getUpdatedAt());
        resumeUpdateDTO.setUpdatedBy(currentResumt.getUpdatedBy());
        return resumeUpdateDTO;
    }

    public void deleteResume(long id){
        Optional<Resume> checkResumeExists = this.resumeRepository.findById(id);
        if (checkResumeExists.isEmpty()){
            throw new IDInvalidException("No exists Resume whose id = " + id);
        }
        this.resumeRepository.deleteById(checkResumeExists.get().getId());
    }

    public ResumeFetchByIdDTO fetchResumeById(long id){
        Optional<Resume> checkResumeExists = this.resumeRepository.findById(id);
        if (checkResumeExists.isEmpty()){
            throw new IDInvalidException("No exists Resume whose id = " + id);
        }
        ResumeFetchByIdDTO resumeFetchByIdDTO = new ResumeFetchByIdDTO();
        resumeFetchByIdDTO.setId(checkResumeExists.get().getId());
        resumeFetchByIdDTO.setEmail(checkResumeExists.get().getEmail());
        resumeFetchByIdDTO.setUrl(checkResumeExists.get().getUrl());
        resumeFetchByIdDTO.setStatus(checkResumeExists.get().getStatus());
        resumeFetchByIdDTO.setCreatedAt(checkResumeExists.get().getCreatedAt());
        resumeFetchByIdDTO.setCreatedBy(checkResumeExists.get().getCreatedBy());
        resumeFetchByIdDTO.setUpdatedAt(checkResumeExists.get().getUpdatedAt());
        resumeFetchByIdDTO.setUpdatedBy(checkResumeExists.get().getUpdatedBy());
        User userOfResume = checkResumeExists.get().getUser();
        Optional<User> checkUserExists = this.userRepository.findById(userOfResume.getId());
        if (checkUserExists.isEmpty()){
            throw new IDInvalidException("No exists user whose id = " + userOfResume.getId());
        }
        UserOfResumeDTO userOfResumeDTO = new UserOfResumeDTO();
        userOfResumeDTO.setId(userOfResume.getId());
        userOfResumeDTO.setName(userOfResume.getName());
        Job jobOfResume = checkResumeExists.get().getJob();
        Optional<Job> checkJobExists = this.jobRepository.findById(jobOfResume.getId());
        if (checkJobExists.isEmpty()){
            throw new IDInvalidException("No exists job whose id = " + jobOfResume.getId());
        }
        JobOfResumeDTO jobOfResumeDTO = new JobOfResumeDTO();
        jobOfResumeDTO.setId(checkJobExists.get().getId());
        jobOfResumeDTO.setName(checkJobExists.get().getName());
        Company company = checkJobExists.get().getCompany();
        Optional<Company> checkCompanyExists = this.companyRepository.findById(company.getId());
        if (checkCompanyExists.isEmpty()){
            throw new IDInvalidException("No exists company whose id = " + company.getId());
        }
        resumeFetchByIdDTO.setCompanyName(checkCompanyExists.get().getName());
        resumeFetchByIdDTO.setUserOfResumeDTO(userOfResumeDTO);
        resumeFetchByIdDTO.setJobOfResumeDTO(jobOfResumeDTO);
        return resumeFetchByIdDTO;
    }

    public ResultPaginationDTO fetchListResumes(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());
        result.setMeta(meta);
        List<ResumeFetchByIdDTO> listResumes = new ArrayList<>();
        for (Resume resume : pageResume.getContent()){
            ResumeFetchByIdDTO resumeFetchByIdDTO = new ResumeFetchByIdDTO();
            resumeFetchByIdDTO.setId(resume.getId());
            resumeFetchByIdDTO.setEmail(resume.getEmail());
            resumeFetchByIdDTO.setUrl(resume.getUrl());
            resumeFetchByIdDTO.setStatus(resume.getStatus());
            resumeFetchByIdDTO.setCreatedAt(resume.getCreatedAt());
            resumeFetchByIdDTO.setCreatedBy(resume.getCreatedBy());
            resumeFetchByIdDTO.setUpdatedAt(resume.getUpdatedAt());
            resumeFetchByIdDTO.setUpdatedBy(resume.getUpdatedBy());
            User userOfResume = resume.getUser();
            Optional<User> checkUserExists = this.userRepository.findById(userOfResume.getId());
            if (checkUserExists.isEmpty()){
                throw new IDInvalidException("No exists user whose id = " + userOfResume.getId());
            }
            UserOfResumeDTO userOfResumeDTO = new UserOfResumeDTO();
            userOfResumeDTO.setId(userOfResume.getId());
            userOfResumeDTO.setName(userOfResume.getName());
            Job jobOfResume = resume.getJob();
            Optional<Job> checkJobExists = this.jobRepository.findById(jobOfResume.getId());
            if (checkJobExists.isEmpty()){
                throw new IDInvalidException("No exists job whose id = " + jobOfResume.getId());
            }
            JobOfResumeDTO jobOfResumeDTO = new JobOfResumeDTO();
            jobOfResumeDTO.setId(checkJobExists.get().getId());
            jobOfResumeDTO.setName(checkJobExists.get().getName());
            Company company = checkJobExists.get().getCompany();
            Optional<Company> checkCompanyExists = this.companyRepository.findById(company.getId());
            if (checkCompanyExists.isEmpty()){
                throw new IDInvalidException("No exists company whose id = " + company.getId());
            }
            resumeFetchByIdDTO.setCompanyName(checkCompanyExists.get().getName());
            resumeFetchByIdDTO.setUserOfResumeDTO(userOfResumeDTO);
            resumeFetchByIdDTO.setJobOfResumeDTO(jobOfResumeDTO);
            listResumes.add(resumeFetchByIdDTO);
        }
        result.setResult(listResumes);
        return result;
    }

    // cho user dang login xem tat ca CV cua minh
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        // remove sensitive data
        List<ResFetchResumeDTO> resFetchResumeDTOS = new ArrayList<>();
        for (Resume resume : pageResume.getContent()){
            ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
            resFetchResumeDTO.setId(resume.getId());
            resFetchResumeDTO.setEmail(resume.getEmail());
            resFetchResumeDTO.setUrl(resume.getUrl());
            resFetchResumeDTO.setStatus(resume.getStatus());
            resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
            resFetchResumeDTO.setCreatedBy(resume.getCreatedBy());
            resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
            resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());
            Company company = resume.getJob().getCompany();
            resFetchResumeDTO.setCompanyName(company.getName());
            User user = resume.getUser();
            ResFetchResumeDTO.UserResume userResume = new ResFetchResumeDTO.UserResume(user.getId(), user.getName());
            Job job = resume.getJob();
            ResFetchResumeDTO.JobResume jobResume = new ResFetchResumeDTO.JobResume(job.getId(), job.getName());
            resFetchResumeDTOS.add(resFetchResumeDTO);
        }
        rs.setResult(resFetchResumeDTOS);
        return rs;
    }
}
