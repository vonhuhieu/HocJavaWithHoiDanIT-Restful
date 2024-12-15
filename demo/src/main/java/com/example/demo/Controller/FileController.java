package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.File.ResponseUploadFileDTO;
import com.example.demo.Service.FileService;
import com.example.demo.Util.Error.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public ResponseEntity<ResponseUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        // skip validate
        if (file == null || file.isEmpty()){
            throw new StorageException("File is empty. Please upload a file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid){
            throw new StorageException("only allows" + allowedExtensions.toString());
        }
        // create a directory if not exist

        this.fileService.createDirectory(baseURI + folder);

        // store file
        String uploadFile = this.fileService.store(file, folder);
        ResponseUploadFileDTO responseUploadFileDTO = new ResponseUploadFileDTO();
        responseUploadFileDTO.setFileName(uploadFile);
        responseUploadFileDTO.setUploadedAt(Instant.now());
        return ResponseEntity.ok().body(responseUploadFileDTO);
    }
}
