package com.loan.loan.service;

import com.loan.loan.dto.CounselDTO.Request;
import com.loan.loan.dto.CounselDTO.Response;

public interface CounselService {

    Response create(Request request);

    Response get(Long counselId);

    Response update(Long counselId, Request request);
}
