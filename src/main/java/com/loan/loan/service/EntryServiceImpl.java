package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Entry;
import com.loan.loan.dto.BalanceDTO;
import com.loan.loan.dto.EntryDTO.Request;
import com.loan.loan.dto.EntryDTO.Response;
import com.loan.loan.dto.EntryDTO.UpdateResponse;
import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final BalanceService balanceService;

    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {

        // 대출 신청 계약 체결 여부 validation
        if(!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);

        // 대출 잔고 관리
        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                        .entryAmount(request.getEntryAmount())
                        .build());

        return modelMapper.map(entry, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);

        if(entry.isPresent()) {
            return modelMapper.map(entry, Response.class);
        } else {
            return null;
        }
    }

    @Override
    public UpdateResponse update(Long entryId, Request request) {
        // entry 존재 여부 validation
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // before -> after
        BigDecimal beforeEntryAmount = entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());

        entryRepository.save(entry);

        // balance update
        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build());

        // response
        return UpdateResponse.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }

    @Override
    public void delete(Long entryId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        entry.setIsDeleted(true);

        entryRepository.save(entry);

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build());
    }

    private boolean isContractedApplication(Long applicationId) {
        Optional<Application> existed = applicationRepository.findById(applicationId);
        if(existed.isEmpty()) {
            return false;
        }

        return existed.get().getContractedAt() != null;
    }
}
