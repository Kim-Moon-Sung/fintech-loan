package com.loan.loan.service;

import com.loan.loan.dto.BalanceDTO.RepaymentRequest;
import com.loan.loan.dto.BalanceDTO.Request;
import com.loan.loan.dto.BalanceDTO.Response;
import com.loan.loan.dto.BalanceDTO.UpdateRequest;

public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);

    void delete(Long applicationId);
}
