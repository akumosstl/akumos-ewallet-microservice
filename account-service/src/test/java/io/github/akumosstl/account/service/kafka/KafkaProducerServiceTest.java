package io.github.akumosstl.account.service.kafka;

import io.github.akumosstl.account.constants.AppConstants;
import io.github.akumosstl.account.dto.WalletResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class KafKaProducerServiceTest {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private KafKaProducerService producerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producerService = new KafKaProducerService();
        producerService.setKafkaTemplate(kafkaTemplate);
    }

    @Test
    void shouldSendMessageToKafkaTopic_andReturnFuture() {
        // Given
        WalletResponseDto walletResponseDto = new WalletResponseDto();
        walletResponseDto.setAccountId(1L);
        walletResponseDto.setWalletId(123L);
        walletResponseDto.setEmail("test@wallet.com");

        // Prepare a mocked SendResult
        SendResult<String, Object> sendResult = mock(SendResult.class);

        // Use SettableListenableFuture to avoid CompletableFuture issue
        SettableListenableFuture<SendResult<String, Object>> future = new SettableListenableFuture<>();
        future.set(sendResult);

        when(kafkaTemplate.send(eq(AppConstants.TOPIC_NAME), any(WalletResponseDto.class)))
                .thenReturn(future);

        // When
        producerService.sendMessage(walletResponseDto);

        // Then
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        verify(kafkaTemplate, times(1)).send(eq(AppConstants.TOPIC_NAME), messageCaptor.capture());

        Object sentMessage = messageCaptor.getValue();
        assertThat(sentMessage).isInstanceOf(WalletResponseDto.class);
        assertThat(((WalletResponseDto) sentMessage).getEmail()).isEqualTo("test@wallet.com");
    }


}

