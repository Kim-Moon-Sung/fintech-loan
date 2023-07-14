package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Terms;
import com.loan.loan.dto.ApplicationDTO;
import com.loan.loan.dto.ApplicationDTO.AcceptTerms;
import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.Response;
import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import com.loan.loan.repository.AcceptTermsRepository;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);
        applicationRepository.save(application);
    }

    @Override
    public Boolean acceptTerms(Long applicationId, AcceptTerms request) {
        // 대출 신청 정보가 존재 하는지 validation
        applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 약관이 존재 하는지 validation
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if(termsList.isEmpty()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 게시한 약관의 수와 고객이 동의한 약관의 수가 일치 하는지 validation
        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if(termsList.size() != acceptTermsIds.size()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
        Collections.sort(acceptTermsIds);

        // 고객이 동의한 약관이 게시한 약관 리스트에 존재 하는지 validation
        if(!termsIds.containsAll(acceptTermsIds)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for(Long termsId : acceptTermsIds) {
            com.loan.loan.domain.AcceptTerms accepted = com.loan.loan.domain.AcceptTerms.builder()
                    .termsId(termsId) // 고객이 동의한 약관의 id
                    .applicationId(applicationId) // 동의한 대출의 id
                    .build();

            acceptTermsRepository.save(accepted);
        }

        return true;
    }
}
