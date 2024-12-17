package com.example.demo.Controller;

import com.example.demo.Domain.*;
import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeFetchByIdDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeUpdateDTO;
import com.example.demo.Service.ResumeService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.ResponseUtil;
import com.example.demo.Util.SecurityUtil;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private ResumeService resumeService;
    private ResponseUtil responseUtil;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, ResponseUtil responseUtil, UserService userService, FilterBuilder filterBuilder, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.responseUtil = responseUtil;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    public ResponseEntity<RestResponse<Object>> createResume(@RequestBody Resume postManResume){
        ResumeDTO resumeDTO = this.resumeService.createResume(postManResume);
        return this.responseUtil.buildCreateResponse("Create a new Resume successfully", resumeDTO);
    }

    @PutMapping("/resumes")
    public ResponseEntity<RestResponse<Object>> updateResume(@RequestBody Resume postmanResume){
        ResumeUpdateDTO resumeUpdateDTO = this.resumeService.updateResume(postmanResume);
        return this.responseUtil.buildSuccessResponse("Update the resume successfully", resumeUpdateDTO);
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<RestResponse<Object>> deleteResume(@PathVariable("id") long id){
        this.resumeService.deleteResume(id);
        return this.responseUtil.buildSuccessResponse("Delete the resume successfully", null);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<RestResponse<Object>> fetchResumeById(@PathVariable("id") long id){
        ResumeFetchByIdDTO resumeFetchByIdDTO = this.resumeService.fetchResumeById(id);
        return this.responseUtil.buildSuccessResponse("Fetch the Resume successfully", resumeFetchByIdDTO);
    }

    @GetMapping("/resumes")
    public ResponseEntity<ResultPaginationDTO> fetchListResumes(@Filter Specification<Resume> specification, Pageable pageable) {
//        ResultPaginationDTO listResumes = this.resumeService.fetchListResumes(specification, pageable);
//        return this.responseUtil.buildSuccessResponse("Get list skills successfully", listResumes);
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleGetUserByUserName(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }
        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());
        Specification<Resume> spec = null;
        Specification<Resume> finalSpec = jobInSpec.and(spec);
        return ResponseEntity.ok().body(this.resumeService.fetchListResumes(finalSpec, pageable));
    }

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable){
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
