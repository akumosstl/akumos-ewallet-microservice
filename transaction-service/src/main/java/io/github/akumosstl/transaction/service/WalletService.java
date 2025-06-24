package io.github.akumosstl.transaction.service;

import io.github.akumosstl.transaction.dto.ApiResponseDto;
import io.github.akumosstl.transaction.model.Wallet;

import java.util.Optional;

public interface WalletService {
    ApiResponseDto fundWallet(double amount, String walletAddress);
    ApiResponseDto withdrawFromWallet(double amount, String walletAddress);
    ApiResponseDto transferToWallet(double amount, String sourceWalletAddress, String destinationWalletAddress);
    Optional<Wallet> findByWalletAddress(String walletAddress);
}
