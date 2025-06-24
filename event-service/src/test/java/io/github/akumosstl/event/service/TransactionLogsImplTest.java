package io.github.akumosstl.event.service;


import io.github.akumosstl.event.dto.ApiResponseDto;
import io.github.akumosstl.event.model.TransactionLogs;
import io.github.akumosstl.event.model.Wallet;
import io.github.akumosstl.event.repository.TransactionLogsRepository;
import io.github.akumosstl.event.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionLogsImplTest {

    @InjectMocks
    private TransactionLogsImpl transactionLogsService;

    @Mock
    private TransactionLogsRepository transactionsLogsRepository;

    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save transaction log and return CREATED response")
    void shouldSaveTransactionLog() {
        TransactionLogs log = new TransactionLogs();
        when(transactionsLogsRepository.save(any(TransactionLogs.class))).thenAnswer(i -> {
            TransactionLogs saved = i.getArgument(0);
            saved.setLogId(1L); // mock ID generation
            return saved;
        });

        ApiResponseDto response = transactionLogsService.saveTransactionLogs(log);

        assertThat(response.getResponseCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getData()).isInstanceOf(TransactionLogs.class);
        verify(transactionsLogsRepository, times(1)).save(any(TransactionLogs.class));
    }

    @Test
    @DisplayName("Should return transaction logs by wallet address")
    void shouldReturnLogsByWalletAddress() {
        String walletAddress = "wallet-123";
        Wallet wallet = new Wallet();
        wallet.setWalletAddress(walletAddress);

        when(walletRepository.findAllByWalletAddress(walletAddress))
                .thenReturn(Optional.of(Collections.singletonList(wallet)));

        ApiResponseDto response = transactionLogsService.getTransactionLogsByWalletAddress(walletAddress);

        assertThat(response.getResponseCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getData()).isInstanceOf(List.class);
        verify(walletRepository).findAllByWalletAddress(walletAddress);
    }

    @Test
    @DisplayName("Should return NOT_FOUND if wallet address not found")
    void shouldReturnNotFoundForWalletAddress() {
        String walletAddress = "wallet-missing";
        when(walletRepository.findAllByWalletAddress(walletAddress)).thenReturn(Optional.empty());

        ApiResponseDto response = transactionLogsService.getTransactionLogsByWalletAddress(walletAddress);

        assertThat(response.getResponseCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getResponseMessage()).contains(walletAddress);
    }

    @Test
    @DisplayName("Should return transaction logs by date range")
    void shouldReturnLogsByDateRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(5);
        LocalDateTime to = LocalDateTime.now();

        Wallet wallet = new Wallet();
        wallet.setWalletAddress("wallet-range");

        when(walletRepository.findByDateCreatedBetween(from, to))
                .thenReturn(Optional.of(List.of(wallet)));

        ApiResponseDto response = transactionLogsService.getTransactionLogsByDateRange(from, to);

        assertThat(response.getResponseCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getData()).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("Should return NOT_FOUND if no transactions in date range")
    void shouldReturnNotFoundForEmptyDateRange() {
        LocalDateTime from = LocalDateTime.now().minusYears(1);
        LocalDateTime to = LocalDateTime.now().minusMonths(6);

        when(walletRepository.findByDateCreatedBetween(from, to)).thenReturn(Optional.empty());

        ApiResponseDto response = transactionLogsService.getTransactionLogsByDateRange(from, to);

        assertThat(response.getResponseCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getResponseMessage()).contains("There are no transactions");
    }
}
