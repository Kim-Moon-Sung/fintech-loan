package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Entry;
import com.loan.loan.domain.Repayment;
import com.loan.loan.dto.BalanceDTO;
import com.loan.loan.dto.BalanceDTO.RepaymentRequest.RepaymentType;
import com.loan.loan.dto.RepaymentDTO.Request;
import com.loan.loan.dto.RepaymentDTO.Response;
import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.EntryRepository;
import com.loan.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;
    private final BalanceService balanceService;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {

        // validation
        // 1. 계약을 완료한 신청 정보
        // 2. 집행이 되어 있어야 함
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);
        repaymentRepository.save(repayment);

        // 잔고 변경
        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(repayment.getRepaymentAmount())
                        .type(RepaymentType.REMOVE)
                        .build());

        Response response = modelMapper.map(repayment, Response.class);
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    private boolean isRepayableApplication(Long applicationId) {
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);
        if (existedApplication.isEmpty()) {
            return false;
        }

        if (existedApplication.get().getContractedAt() == null) {
            return false;
        }

        Optional<Entry> existedEntry = entryRepository.findByApplicationId(applicationId);
        return existedEntry.isPresent();
    }
}
