package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.Job.JobDTO;
import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.Job;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.JobService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private JobService jobService;
    private ResponseUtil responseUtil;

    public JobController(JobService jobService, ResponseUtil responseUtil) {
        this.jobService = jobService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/jobs")
    public ResponseEntity<RestResponse<Object>> createJob(@RequestBody Job postManJob){
        JobDTO newJob = this.jobService.createJob(postManJob);
        return this.responseUtil.buildCreateResponse("create a new job successfully", newJob);
    }

    @PutMapping("/jobs")
    public ResponseEntity<RestResponse<Object>> updateJob(@RequestBody Job postManJob){
        JobDTO currentJob = this.jobService.updateJob(postManJob);
        return this.responseUtil.buildSuccessResponse("update the job successfully", currentJob);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<RestResponse<Object>> deleteJob(@PathVariable("id") long id){
        this.jobService.deleteJob(id);
        return this.responseUtil.buildSuccessResponse("Delete the job successfully", null);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<RestResponse<Object>> fetchJobById(@PathVariable("id") long id){
        JobDTO jobDTO = this.jobService.fetchJobById(id);
        return this.responseUtil.buildSuccessResponse("fetch the job whose id = " + id + " successfully", jobDTO);
    }

    @GetMapping("/jobs")
    public ResponseEntity<RestResponse<Object>> fetchListJobs(@Filter Specification<Job> specification, Pageable pageable) {
        ResultPaginationDTO listJobs = this.jobService.fetchListJobs(specification, pageable);
        return this.responseUtil.buildSuccessResponse("Get list skills successfully", listJobs);
    }
}
