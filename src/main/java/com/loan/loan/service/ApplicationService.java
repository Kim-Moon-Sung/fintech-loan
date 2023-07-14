package com.loan.loan.service;

import com.loan.loan.dto.ApplicationDTO;
import com.loan.loan.dto.ApplicationDTO.AcceptTerms;
import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.Response;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);

    Boolean acceptTerms(Long applicationId, AcceptTerms request);
}
