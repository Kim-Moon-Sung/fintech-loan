package com.loan.loan.service;

import com.loan.loan.domain.Terms;
import com.loan.loan.dto.TermsDTO;
import com.loan.loan.dto.TermsDTO.Response;
import com.loan.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(TermsDTO.Request request) {
        Terms terms = modelMapper.map(request, Terms.class);
        Terms created = termsRepository.save(terms);
        return modelMapper.map(created, Response.class);
    }
}
