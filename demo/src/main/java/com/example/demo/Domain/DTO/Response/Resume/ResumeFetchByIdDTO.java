package com.example.demo.Domain.DTO.Response.Resume;

import com.example.demo.Util.Enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ResumeFetchByIdDTO {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    @JsonProperty("company")
    private String companyName;

    @JsonProperty("user")
    private UserOfResumeDTO userOfResumeDTO;

    @JsonProperty("job")
    private JobOfResumeDTO jobOfResumeDTO;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }


    public UserOfResumeDTO getUserOfResumeDTO() {
        return userOfResumeDTO;
    }

    public void setUserOfResumeDTO(UserOfResumeDTO userOfResumeDTO) {
        this.userOfResumeDTO = userOfResumeDTO;
    }

    public JobOfResumeDTO getJobOfResumeDTO() {
        return jobOfResumeDTO;
    }

    public void setJobOfResumeDTO(JobOfResumeDTO jobOfResumeDTO) {
        this.jobOfResumeDTO = jobOfResumeDTO;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
