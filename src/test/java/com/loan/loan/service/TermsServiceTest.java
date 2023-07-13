package com.loan.loan.service;

import com.loan.loan.domain.Terms;
import com.loan.loan.dto.TermsDTO.Request;
import com.loan.loan.dto.TermsDTO.Response;
import com.loan.loan.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {

    @InjectMocks TermsServiceImpl termsService;

    @Mock private TermsRepository termsRepository;
    @Spy private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewTermsEntity_When_RequestTerms() {

        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/sdafgdsfasd")
                .build();

        Request request = Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/sdafgdsfasd")
                .build();

        when(termsRepository.save(any(Terms.class))).thenReturn(entity);

        Response actual = termsService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(entity.getTermsDetailUrl());
    }
}
