package io.github.akumosstl.account.repository;

import io.github.akumosstl.account.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
