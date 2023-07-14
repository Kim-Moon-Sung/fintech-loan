package com.loan.loan.service;

import com.loan.loan.dto.TermsDTO.Request;
import com.loan.loan.dto.TermsDTO.Response;

import java.util.List;

public interface TermsService {

    Response create(Request request);

    List<Response> getAll();
}
