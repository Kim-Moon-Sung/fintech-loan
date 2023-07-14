package com.loan.loan.service;

import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Resource load(String fileName) {

        try {
            Path file = Paths.get(uploadPath).resolve(fileName);
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
    public Stream<Path> loadAll() {
        try {
            // 지정한 Paths 의 정해진 Depth 에 있는 파일들 만을 가져 오도록 하였음
            return Files.walk(Paths.get(uploadPath), 1).filter(path -> !path.equals(Paths.get(uploadPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }
}
