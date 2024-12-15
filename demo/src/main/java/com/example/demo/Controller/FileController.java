package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.File.ResponseUploadFileDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Service.FileService;
import com.example.demo.Util.Error.StorageException;
import com.example.demo.Util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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
    private ResponseUtil responseUtil;

    public FileController(FileService fileService, ResponseUtil responseUtil) {
        this.fileService = fileService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/files")
    public ResponseEntity<RestResponse<Object>> upload(
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
        return this.responseUtil.buildSuccessResponse("Upload a file successfully", responseUploadFileDTO);
    }

    @GetMapping("/files")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new StorageException("Missing required params : (fileName or folder) in query params.");
        }
        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File with name = " + fileName + " not found.");
        }
        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
//        return this.responseUtil.buildSuccessResponse("download a file successfully", resource);
    }
}
