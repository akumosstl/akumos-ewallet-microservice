package io.github.akumosstl.event.service;

import io.github.akumosstl.event.dto.ApiResponseDto;
import io.github.akumosstl.event.model.TransactionLogs;

import java.time.LocalDateTime;

public interface TransactionLogsService {
  ApiResponseDto saveTransactionLogs(TransactionLogs data);
  ApiResponseDto getTransactionLogsByWalletAddress(String walletAddress);
  ApiResponseDto getTransactionLogsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo);
}
