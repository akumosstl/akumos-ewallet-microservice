package io.github.akumosstl.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.event.dto.ApiResponseDto;
import io.github.akumosstl.event.dto.TransactionLogRequestByDateRangeDto;
import io.github.akumosstl.event.dto.TransactionLogRequestByWalletAddressDto;
import io.github.akumosstl.event.service.TransactionLogsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventsController.class)
class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionLogsService logsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /v1/logs/walletAddress - should return logs by wallet address")
    void shouldReturnLogsByWalletAddress() throws Exception {
        // Arrange
        String walletAddress = "wallet123";
        ApiResponseDto responseDto = new ApiResponseDto();
        responseDto.setResponseCode(200);
        responseDto.setResponseMessage("Success");

        Mockito.when(logsService.getTransactionLogsByWalletAddress(eq(walletAddress)))
                .thenReturn(responseDto);

        TransactionLogRequestByWalletAddressDto requestDto = new TransactionLogRequestByWalletAddressDto();
        requestDto.setWalletAddress(walletAddress);

        // Act & Assert
        mockMvc.perform(post("/v1/logs/walletAddress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("Success"));
    }

    @Test
    @DisplayName("POST /v1/logs/date - should return logs by date range")
    void shouldReturnLogsByDateRange() throws Exception {
        // Arrange
        LocalDate dateFrom = LocalDate.of(2023, 1, 1);
        LocalDate dateTo = LocalDate.of(2023, 1, 31);

        ApiResponseDto responseDto = new ApiResponseDto();
        responseDto.setResponseCode(200);
        responseDto.setResponseMessage("Success");

        Mockito.when(logsService.getTransactionLogsByDateRange(dateFrom.atStartOfDay(), dateTo.atStartOfDay()))
                .thenReturn(responseDto);

        TransactionLogRequestByDateRangeDto requestDto = new TransactionLogRequestByDateRangeDto();
        requestDto.setDateFrom(dateFrom.atStartOfDay());
        requestDto.setDateTo(dateTo.atStartOfDay());

        // Act & Assert
        mockMvc.perform(post("/v1/logs/date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("Success"));
    }
}

