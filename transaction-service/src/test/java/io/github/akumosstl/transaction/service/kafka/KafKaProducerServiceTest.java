package io.github.akumosstl.transaction.service.kafka;

import io.github.akumosstl.transaction.constants.AppConstants;
import io.github.akumosstl.transaction.dto.WalletResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@Disabled
class KafKaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafKaProducerService producerService;

    private WalletResponseDto walletResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletResponseDto = new WalletResponseDto();
        walletResponseDto.setWalletAddress("wallet-123");
        walletResponseDto.setEmail("user@example.com");
        walletResponseDto.setAmount(100.0);
    }

    @Test
    void testSendFundSuccessDetails() {
        producerService.sendFundSuccessDetails(walletResponseDto);

        verify(kafkaTemplate, times(1)).send(AppConstants.FUNDING_TOPIC_NAME, walletResponseDto);
    }

    @Test
    void testSendWithdrawalSuccessDetails() {
        producerService.sendWithdrawalSuccessDetails(walletResponseDto);

        verify(kafkaTemplate, times(1)).send(AppConstants.WITHDRAWAL_TOPIC_NAME, walletResponseDto);
    }

    @Test
    void testSendTransferSuccessDetails() {
        producerService.sendTransferSuccessDetails(walletResponseDto);

        verify(kafkaTemplate, times(1)).send(AppConstants.TRANSFER_TOPIC_NAME, walletResponseDto);
    }
}

