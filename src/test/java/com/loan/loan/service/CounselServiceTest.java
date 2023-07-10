package com.loan.loan.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.loan.loan.domain.Counsel;
import com.loan.loan.dto.CounselDTO.Request;
import com.loan.loan.dto.CounselDTO.Response;
import com.loan.loan.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        Counsel entity = Counsel.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("대출을 받고 싶어요. 연락 주세요.")
                .zipCode("12345")
                .address("서울특별시 어디구 모르동")
                .addressDetail("101동 101호")
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("abc@def.g")
                .memo("대출을 받고 싶어요. 연락 주세요.")
                .zipCode("12345")
                .address("서울특별시 어디구 모르동")
                .addressDetail("101동 101호")
                .build();

        when(counselRepository.save(any(Counsel.class))).thenReturn(entity);

        Response actual = counselService.create(request);
        assertThat(actual.getName()).isSameAs(entity.getName());
    }
}
