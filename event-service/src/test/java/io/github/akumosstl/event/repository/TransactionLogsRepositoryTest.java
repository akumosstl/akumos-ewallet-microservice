package io.github.akumosstl.event.repository;


import io.github.akumosstl.event.model.TransactionLogs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionLogsRepositoryTest {

    @Autowired
    private TransactionLogsRepository transactionLogsRepository;

    @Test
    @DisplayName("Save and find TransactionLogs by ID")
    void shouldSaveAndFindTransactionLogs() {
        // Given
        TransactionLogs log = new TransactionLogs();
        // Set required fields, example:
        log.setLogId(1L);
        log.setWalletAddress("walletXYZ");
        log.setAmount(150.75);
        log.setDate(LocalDateTime.now());

        // When
        TransactionLogs savedLog = transactionLogsRepository.save(log);
        Optional<TransactionLogs> foundLog = transactionLogsRepository.findById(savedLog.getLogId());

        // Then
        assertThat(foundLog).isPresent();
        assertThat(foundLog.get().getLogId()).isEqualTo(1L);
        assertThat(foundLog.get().getWalletAddress()).isEqualTo("walletXYZ");
        assertThat(foundLog.get().getAmount()).isEqualTo(150.75);
    }
}

