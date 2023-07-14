package com.loan.loan.controller;

import com.loan.loan.dto.ApplicationDTO.AcceptTerms;
import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.Response;
import com.loan.loan.dto.FileDTO;
import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.service.ApplicationServiceImpl;
import com.loan.loan.service.FileStorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

    private final ApplicationServiceImpl applicationService;
    private final FileStorageServiceImpl fileStorageService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(applicationService.create(request));
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        return ok(applicationService.get(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<Response> update(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(applicationService.update(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Response> delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ok();
    }

    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId, @RequestBody AcceptTerms request) {
        return ok(applicationService.acceptTerms(applicationId, request));
    }

    @PostMapping("/{applicationId}/files")
    public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) {
        fileStorageService.save(applicationId, file);
        return ok();
    }

    @GetMapping("/{applicationId}/files")
    public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/{applicationId}/files")
    public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

    @GetMapping("/{applicationId}/files/info")
    public ResponseDTO<List<FileDTO>> getFileInfos(@PathVariable Long applicationId) {
        List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "download", applicationId, fileName).build().toString())
                    .build();
        }).collect(Collectors.toList());

        return ok(fileInfos);
    }
}
