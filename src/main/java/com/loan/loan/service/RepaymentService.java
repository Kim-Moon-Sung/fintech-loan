package com.loan.loan.service;

import com.loan.loan.dto.RepaymentDTO.Request;
import com.loan.loan.dto.RepaymentDTO.Response;

public interface RepaymentService {

    Response create(Long applicationId, Request request);
}
