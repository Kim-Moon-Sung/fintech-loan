package com.loan.loan.service;

import com.loan.loan.dto.RepaymentDTO.ListResponse;
import com.loan.loan.dto.RepaymentDTO.Request;
import com.loan.loan.dto.RepaymentDTO.Response;

import java.util.List;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> get(Long applicationId);
}
