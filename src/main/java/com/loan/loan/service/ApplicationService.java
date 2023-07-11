package com.loan.loan.service;

import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.Response;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);
}
