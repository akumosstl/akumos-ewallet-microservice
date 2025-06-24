package io.github.akumosstl.transaction.repository;

import io.github.akumosstl.transaction.model.Wallet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    @DisplayName("Should save and retrieve wallet by wallet address")
    void shouldSaveAndFindByWalletAddress() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setWalletAddress("wallet-xyz-123");
        wallet.setWalletBalance(1000.0);
        wallet.setDateCreated(LocalDateTime.now());

        walletRepository.save(wallet);

        // Act
        Optional<Wallet> found = walletRepository.findByWalletAddress("wallet-xyz-123");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getWalletBalance()).isEqualTo(1000.0);
        assertThat(found.get().getWalletAddress()).isEqualTo("wallet-xyz-123");
    }
}

