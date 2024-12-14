package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.JobDTO;
import com.example.demo.Domain.Job;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Service.JobService;
import com.example.demo.Util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
