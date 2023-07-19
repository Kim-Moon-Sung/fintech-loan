package com.loan.loan.service;

import com.loan.loan.dto.ApplicationDTO.GrantAmount;
import com.loan.loan.dto.JudgmentDTO.Request;
import com.loan.loan.dto.JudgmentDTO.Response;

public interface JudgmentService {

    Response create(Request request);

    Response get(Long judgementId);

    Response getJudgementOfApplication(Long applicationId);

    Response update(Long judgmentId, Request request);

    void delete(Long judgmentId);

    GrantAmount grant(Long judgmentId);
}
