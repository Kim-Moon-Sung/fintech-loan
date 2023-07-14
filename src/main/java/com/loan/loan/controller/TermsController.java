package com.loan.loan.controller;

import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.dto.TermsDTO.Request;
import com.loan.loan.dto.TermsDTO.Response;
import com.loan.loan.service.TermsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terms")
public class TermsController extends AbstractController {

    private final TermsServiceImpl termsService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<Response>> getAll() {
        return ok(termsService.getAll());
    }
}
