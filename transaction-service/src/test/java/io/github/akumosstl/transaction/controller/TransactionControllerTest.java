package io.github.akumosstl.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.transaction.dto.*;
import io.github.akumosstl.transaction.service.WalletService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should fund wallet")
    void shouldFundWallet() throws Exception {
        FundWalletRequestDto request = new FundWalletRequestDto();
        request.setAmount(100.0);
        request.setWalletAddress("wallet-123");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Funded");
        response.setData(new WalletDto());

        Mockito.when(walletService.fundWallet(eq(100.0), eq("wallet-123")))
                .thenReturn(response);

        mockMvc.perform(post("/v1/wallet/fund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Funded"));
    }

    @Test
    @DisplayName("Should withdraw from wallet")
    void shouldWithdrawFromWallet() throws Exception {
        WithdrawWalletRequestDto request = new WithdrawWalletRequestDto();
        request.setAmount(50.0);
        request.setWalletAddress("wallet-123");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Withdrawn");
        response.setData(new WalletDto());

        Mockito.when(walletService.withdrawFromWallet(eq(50.0), eq("wallet-123")))
                .thenReturn(response);

        mockMvc.perform(post("/v1/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Withdrawn"));
    }

    @Test
    @DisplayName("Should transfer between wallets")
    void shouldTransferFromWalletToWallet() throws Exception {
        WalletTransferRequestDto request = new WalletTransferRequestDto();
        request.setAmount(30.0);
        request.setSourceWalletAddress("wallet-123");
        request.setDestinationWalletAddress("wallet-456");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Transferred");
        response.setData(new WalletDto());

        Mockito.when(walletService.transferToWallet(
                        eq(30.0),
                        eq("wallet-123"),
                        eq("wallet-456")))
                .thenReturn(response);

        mockMvc.perform(post("/v1/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Transferred"));
    }
}
