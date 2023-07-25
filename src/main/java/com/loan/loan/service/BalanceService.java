package com.loan.loan.service;

import com.loan.loan.dto.BalanceDTO.Request;
import com.loan.loan.dto.BalanceDTO.Response;

public interface BalanceService {

    Response create(Long applicationId, Request request);
}
