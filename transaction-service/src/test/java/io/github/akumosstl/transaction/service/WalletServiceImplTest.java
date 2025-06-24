package io.github.akumosstl.transaction.service;

import io.github.akumosstl.transaction.dto.ApiResponseDto;
import io.github.akumosstl.transaction.dto.WalletResponseDto;
import io.github.akumosstl.transaction.model.Account;
import io.github.akumosstl.transaction.model.Wallet;
import io.github.akumosstl.transaction.repository.WalletRepository;
import io.github.akumosstl.transaction.service.kafka.KafKaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private KafKaProducerService producerService;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountNo("ACC123");
        account.setEmail("user@example.com");
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setSurname("Smith");
        account.setPhone("1234567890");

        wallet = new Wallet();
        wallet.setWalletId(1L);
        wallet.setWalletAddress("wallet123");
        wallet.setWalletBalance(100.0);
        wallet.setAccount(account);
    }

    @Test
    void testFundWallet_Success() {
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        ApiResponseDto response = walletService.fundWallet(50.0, "wallet123");

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Funding was successful", response.getResponseMessage());
        assertNotNull(response.getData());

        verify(producerService).sendFundSuccessDetails(any(WalletResponseDto.class));
    }

    @Test
    void testFundWallet_NotFound() {
        when(walletRepository.findByWalletAddress("invalid")).thenReturn(Optional.empty());

        ApiResponseDto response = walletService.fundWallet(50.0, "invalid");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponseCode());
        assertEquals("Wallet Address does not exist", response.getResponseMessage());
    }

    @Test
    void testWithdrawWallet_InsufficientFunds() {
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));

        ApiResponseDto response = walletService.withdrawFromWallet(200.0, "wallet123");

        assertEquals(HttpStatus.CONFLICT.value(), response.getResponseCode());
        assertEquals("Insufficient Funds", response.getResponseMessage());
    }

    @Test
    void testWithdrawWallet_Success() {
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        ApiResponseDto response = walletService.withdrawFromWallet(50.0, "wallet123");

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Withdrawal was successful", response.getResponseMessage());
        verify(producerService).sendWithdrawalSuccessDetails(any(WalletResponseDto.class));
    }

    @Test
    void testTransferWallet_Success() {
        Wallet destWallet = new Wallet();
        destWallet.setWalletId(2L);
        destWallet.setWalletAddress("wallet456");
        destWallet.setWalletBalance(10.0);
        destWallet.setAccount(wallet.getAccount());

        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));
        when(walletRepository.findByWalletAddress("wallet456")).thenReturn(Optional.of(destWallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        ApiResponseDto response = walletService.transferToWallet(20.0, "wallet123", "wallet456");

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Fund transfer was successful", response.getResponseMessage());
        verify(producerService).sendWithdrawalSuccessDetails(any(WalletResponseDto.class));
    }

    @Test
    void testTransferWallet_SourceNotFound() {
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.empty());

        ApiResponseDto response = walletService.transferToWallet(20.0, "wallet123", "wallet456");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponseCode());
        assertEquals("Debit wallet address is not found", response.getResponseMessage());
    }

    @Test
    void testTransferWallet_DestinationNotFound() {
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));
        when(walletRepository.findByWalletAddress("wallet456")).thenReturn(Optional.empty());

        ApiResponseDto response = walletService.transferToWallet(20.0, "wallet123", "wallet456");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponseCode());
        assertEquals("Credit wallet address is not found", response.getResponseMessage());
    }

    @Test
    void testTransferWallet_InsufficientFunds() {
        wallet.setWalletBalance(10.0);
        when(walletRepository.findByWalletAddress("wallet123")).thenReturn(Optional.of(wallet));

        ApiResponseDto response = walletService.transferToWallet(50.0, "wallet123", "wallet456");

        assertEquals(HttpStatus.CONFLICT.value(), response.getResponseCode());
        assertEquals("Insufficient Funds", response.getResponseMessage());
    }
}

