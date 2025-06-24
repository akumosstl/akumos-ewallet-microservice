package io.github.akumosstl.event.service.kafka;

import io.github.akumosstl.event.dto.WalletResponseDto;
import io.github.akumosstl.event.service.TransactionLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@Disabled
@SpringBootTest
class KafKaConsumerServiceTest {

    @InjectMocks
    private KafKaConsumerService consumerService;

    @Mock
    private TransactionLogsService logsService;

    private WalletResponseDto mockMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMessage = new WalletResponseDto();
        mockMessage.setAccountNo("12345678");
        mockMessage.setEmail("user@example.com");
        mockMessage.setFirstName("Alisson");
        mockMessage.setLastName("Pedrina");
        mockMessage.setPhone("999999999");
        mockMessage.setWalletAddress("wallet-xyz");
        mockMessage.setWalletBalance(500.0);
        mockMessage.setAmount(100.0);
        mockMessage.setDate(LocalDateTime.now());
        mockMessage.setSurname("Silva");
    }

    @Test
    @DisplayName("Should send funding notification and save transaction log")
    void shouldHandleFundingMessage() {
        consumerService.consumeFundSuccessDetails(mockMessage);

        verify(logsService).saveTransactionLogs(argThat(log ->
                log.getTransactionType().equals("DEPOSIT") &&
                        log.getAmount().equals(100.0) &&
                        log.getWalletAddress().equals("wallet-xyz")
        ));
    }

    @Test
    @DisplayName("Should send withdrawal notification and save transaction log")
    void shouldHandleWithdrawalMessage() {
        consumerService.consumeWithdrawalSuccessDetails(mockMessage);

        verify(logsService).saveTransactionLogs(argThat(log ->
                log.getTransactionType().equals("WITHDRAWAL") &&
                        log.getWalletAddress().equals("wallet-xyz")
        ));
    }

    @Test
    @DisplayName("Should send transfer notification and save transaction log")
    void shouldHandleTransferMessage() {
        consumerService.consumeTransferSuccessDetails(mockMessage);

        verify(logsService).saveTransactionLogs(argThat(log ->
                log.getTransactionType().equals("TRANSFER") &&
                        log.getWalletAddress().equals("wallet-xyz")
        ));
    }

}
