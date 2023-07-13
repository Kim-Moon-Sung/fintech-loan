package com.loan.loan.service;

import com.loan.loan.dto.TermsDTO.Request;
import com.loan.loan.dto.TermsDTO.Response;

public interface TermsService {

    Response create(Request request);
}
