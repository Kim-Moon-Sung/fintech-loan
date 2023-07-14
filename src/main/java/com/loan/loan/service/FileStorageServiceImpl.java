package com.loan.loan.service;

import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import com.loan.loan.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final ApplicationRepository applicationRepository;

    @Override
    public void save(Long applicationId, MultipartFile file) {
        if(!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path directoryPath = Path.of(applicationPath);
            if(!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.copy(file.getInputStream(), Paths.get(applicationPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Resource load(Long applicationId, String fileName) {
        if(!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path file = Paths.get(applicationPath).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if(resource.isReadable() || resource.exists()) {
                return resource;
            } else {
                throw new BaseException(ResultType.NOT_EXIST);
            }
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Stream<Path> loadAll(Long applicationId) {
        if(!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            // 지정한 Paths 의 정해진 Depth 에 있는 파일들 만을 가져 오도록 하였음
            return Files.walk(Paths.get(applicationPath), 1).filter(path -> !path.equals(Paths.get(applicationPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public void deleteAll(Long applicationId) {
        if(!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        String applicationPath = uploadPath.concat("/" + applicationId);
        // uploadPath 에 존재하는 모든 파일들을 삭제하도록 구현
        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile());
    }

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
