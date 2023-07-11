package com.loan.loan.controller;

import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.Response;
import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.service.ApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

    private final ApplicationServiceImpl applicationService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(applicationService.create(request));
    }
}
