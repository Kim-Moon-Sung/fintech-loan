package com.loan.loan.service;

import com.loan.loan.dto.EntryDTO.Request;
import com.loan.loan.dto.EntryDTO.Response;

public interface EntryService {

    Response create(Long applicationId, Request request);
}
