package com.loan.loan.controller;

import com.loan.loan.dto.JudgmentDTO.Request;
import com.loan.loan.dto.JudgmentDTO.Response;
import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.service.JudgementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/judgements")
public class JudgementController extends AbstractController {

    private final JudgementServiceImpl judgementService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(judgementService.create(request));
    }

    @GetMapping("/{judgementId}")
    public ResponseDTO<Response> get(@PathVariable Long judgementId) {
        return ok(judgementService.get(judgementId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<Response> getJudgementOfApplication(@PathVariable Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @PutMapping("/{judgmentId}")
    public ResponseDTO<Response> update(@PathVariable Long judgmentId, @RequestBody Request request) {
        return ok(judgementService.update(judgmentId, request));
    }

    @DeleteMapping("/{judgmentId}")
    public ResponseDTO<Void> delete(@PathVariable Long judgmentId) {
        judgementService.delete(judgmentId);
        return ok();
    }
}
