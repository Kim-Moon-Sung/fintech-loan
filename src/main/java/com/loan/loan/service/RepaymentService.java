package com.loan.loan.service;

import com.loan.loan.dto.RepaymentDTO.ListResponse;
import com.loan.loan.dto.RepaymentDTO.Request;
import com.loan.loan.dto.RepaymentDTO.Response;
import com.loan.loan.dto.RepaymentDTO.UpdateResponse;

import java.util.List;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> get(Long applicationId);

    UpdateResponse update(Long repaymentId, Request request);
}
