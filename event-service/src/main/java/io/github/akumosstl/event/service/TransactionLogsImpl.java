package io.github.akumosstl.event.service;


import io.github.akumosstl.event.dto.ApiResponseDto;
import io.github.akumosstl.event.model.TransactionLogs;
import io.github.akumosstl.event.model.Wallet;
import io.github.akumosstl.event.repository.TransactionLogsRepository;
import io.github.akumosstl.event.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionLogsImpl implements TransactionLogsService {

    @Autowired
    TransactionLogsRepository transactionsLogsRepository;

    @Autowired
    WalletRepository walletRepository;

    @Override
    public ApiResponseDto saveTransactionLogs(TransactionLogs data) {
        ApiResponseDto dto = new ApiResponseDto();
        data.setDate(LocalDateTime.now());
        TransactionLogs saved = transactionsLogsRepository.save(data);

        dto.setData(saved);
        dto.setResponseCode(HttpStatus.CREATED.value());
        dto.setResponseMessage("TransactionLog has been saved Successfully");
        return dto;

    }

    @Override
    public ApiResponseDto getTransactionLogsByWalletAddress(String walletAddress) {
        Optional<List<Wallet>> transactionList = walletRepository.findAllByWalletAddress(walletAddress);
        ApiResponseDto dto = new ApiResponseDto();

        if (transactionList.isPresent()) {
            dto.setResponseMessage("Success");
            dto.setResponseCode(HttpStatus.OK.value());
            dto.setData(transactionList.get());
            return dto;
        }

        dto.setResponseMessage("No transaction found for wallet Address: " + walletAddress);
        dto.setResponseCode(HttpStatus.NOT_FOUND.value());
        return dto;
    }

    @Override
    public ApiResponseDto getTransactionLogsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        Optional<List<Wallet>> transactionList = walletRepository.findByDateCreatedBetween(dateFrom, dateTo);
        ApiResponseDto dto = new ApiResponseDto();

        if (transactionList.isPresent()) {
            dto.setData(transactionList.get());
            dto.setResponseCode(HttpStatus.OK.value());
            dto.setResponseMessage("Success");
            return dto;
        }

        dto.setResponseCode(HttpStatus.NOT_FOUND.value());
        dto.setResponseMessage("There are no transactions from " + dateFrom + " to " + dateTo);
        return dto;
    }





}
