package io.github.akumosstl.account.service;

import io.github.akumosstl.account.dto.AccountRequestDto;
import io.github.akumosstl.account.dto.ApiResponseDto;
import io.github.akumosstl.account.dto.WalletResponseDto;
import io.github.akumosstl.account.model.Account;
import io.github.akumosstl.account.model.Wallet;
import io.github.akumosstl.account.repository.AccountRepository;
import io.github.akumosstl.account.repository.WalletRepository;
import io.github.akumosstl.account.service.AccountServiceImpl;
import io.github.akumosstl.account.service.kafka.KafKaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private WalletRepository walletRepository;
    private KafKaProducerService kafKaProducerService;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        walletRepository = mock(WalletRepository.class);
        kafKaProducerService = mock(KafKaProducerService.class);

        accountService = new AccountServiceImpl(kafKaProducerService);
        accountService.setAccountRepository(accountRepository);
        accountService.setWalletRepository(walletRepository);
    }

    @Test
    void shouldCreateNewAccount_whenEmailAndPhoneNotExists() {
        AccountRequestDto request = new AccountRequestDto();
        request.setAccountId(1L);
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");
        request.setSurname("Jr");

        when(accountRepository.findByEmailOrPhone("test@example.com", "1234567890"))
                .thenReturn(Optional.empty());

        Account savedAccount = new Account();
        savedAccount.setAccountId(1L);
        savedAccount.setAccountNo("1234567");
        savedAccount.setEmail("test@example.com");
        savedAccount.setFirstName("John");
        savedAccount.setLastName("Doe");
        savedAccount.setPhone("1234567890");
        savedAccount.setSurname("Jr");

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Wallet wallet = new Wallet();
        wallet.setWalletId(1L);
        wallet.setWalletAddress("addr-abc");
        wallet.setWalletBalance(0.00);
        wallet.setAccount(savedAccount);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        ApiResponseDto response = accountService.createAccount(request);

        assertThat(response.getResponseCode()).isEqualTo(201);
        assertThat(response.getResponseMessage()).contains("successful");
        assertThat(response.getData()).isInstanceOf(WalletResponseDto.class);

        WalletResponseDto walletDto = (WalletResponseDto) response.getData();
        assertThat(walletDto.getEmail()).isEqualTo("test@example.com");

        verify(kafKaProducerService).sendMessage(any(WalletResponseDto.class));
    }

    @Test
    void shouldNotCreateAccount_whenEmailOrPhoneExists() {
        AccountRequestDto request = new AccountRequestDto();
        request.setEmail("test@example.com");
        request.setPhone("1234567890");

        Account existing = new Account();
        when(accountRepository.findByEmailOrPhone("test@example.com", "1234567890"))
                .thenReturn(Optional.of(existing));

        ApiResponseDto response = accountService.createAccount(request);

        assertThat(response.getResponseCode()).isEqualTo(409);
        assertThat(response.getResponseMessage()).contains("Already exist");

        verify(accountRepository, never()).save(any(Account.class));
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(kafKaProducerService, never()).sendMessage(any());
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        AccountRequestDto request = new AccountRequestDto();
        request.setAccountId(2L);
        request.setAccountNo("999999");
        request.setEmail("update@example.com");
        request.setFirstName("Updated");
        request.setLastName("User");
        request.setPhone("9876543210");
        request.setSurname("Sr");

        Account updated = new Account();
        updated.setAccountId(request.getAccountId());

        when(accountRepository.save(any(Account.class))).thenReturn(updated);

        ApiResponseDto response = accountService.updateAccount(request);

        assertThat(response.getResponseCode()).isEqualTo(200);
        assertThat(response.getResponseMessage()).contains("successful");
        assertThat(response.getData()).isInstanceOf(Account.class);

        Account result = (Account) response.getData();
        assertThat(result.getAccountId()).isEqualTo(2L);
    }
}

