package io.github.akumosstl.event.repository;

import io.github.akumosstl.event.model.TransactionLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, Long> {
}
