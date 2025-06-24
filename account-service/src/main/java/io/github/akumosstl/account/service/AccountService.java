package io.github.akumosstl.account.service;

import io.github.akumosstl.account.dto.AccountRequestDto;
import io.github.akumosstl.account.dto.ApiResponseDto;

public interface AccountService {
    ApiResponseDto createAccount(AccountRequestDto dto);

    ApiResponseDto updateAccount(AccountRequestDto dto);
}
