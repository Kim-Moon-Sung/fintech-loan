package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Judgment;
import com.loan.loan.dto.JudgmentDTO.Request;
import com.loan.loan.dto.JudgmentDTO.Response;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.JudgementRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JudgementServiceTest {

    @InjectMocks private JudgementServiceImpl judgementService;

    @Mock private ApplicationRepository applicationRepository;
    @Mock
    private JudgementRepository judgementRepository;

    @Spy private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewJudgementEntity_When_RequestNewJudgment() {
        Request request = Request.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        Judgment entity = Judgment.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgementRepository.save(any(Judgment.class))).thenReturn(entity);

        Response actual = judgementService.create(request);
        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getApplicationId()).isSameAs(entity.getApplicationId());
        assertThat(actual.getApprovalAmount()).isSameAs(entity.getApprovalAmount());
    }

    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistJudgment() {

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));

        Response actual = judgementService.get(1L);

        assertThat(actual.getJudgmentId()).isSameAs(1L);
    }

    @Test
    void Should_ReturnResponseOfExistJudgementEntity_When_RequestExistApplicationId() {

        Judgment judgementEntity = Judgment.builder()
                .judgmentId(1L)
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(applicationEntity));
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.ofNullable(judgementEntity));

        Response actual = judgementService.getJudgementOfApplication(1L);

        assertThat(actual.getJudgmentId()).isSameAs(1L);
    }
}
