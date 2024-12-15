package com.example.demo.Domain.DTO.Response.File;


import java.time.Instant;

public class ResponseUploadFileDTO {
    private String fileName;
    private Instant uploadedAt;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
