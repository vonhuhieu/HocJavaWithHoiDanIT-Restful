package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeFetchByIdDTO;
import com.example.demo.Domain.DTO.Response.Resume.ResumeUpdateDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.Resume;
import com.example.demo.Service.ResumeService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private ResumeService resumeService;
    private ResponseUtil responseUtil;

    public ResumeController(ResumeService resumeService, ResponseUtil responseUtil) {
        this.resumeService = resumeService;
        this.responseUtil = responseUtil;
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
    public ResponseEntity<RestResponse<Object>> fetchListResumes(@Filter Specification<Resume> specification, Pageable pageable) {
        ResultPaginationDTO listResumes = this.resumeService.fetchListResumes(specification, pageable);
        return this.responseUtil.buildSuccessResponse("Get list skills successfully", listResumes);
    }
}
