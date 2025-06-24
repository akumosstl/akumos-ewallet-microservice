package io.github.akumosstl.event.repository;

import io.github.akumosstl.event.model.Wallet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    @DisplayName("Should find Wallet by wallet address")
    void shouldFindByWalletAddress() {
        // Given
        Wallet wallet = new Wallet();
        wallet.setWalletAddress("wallet-abc-123");
        wallet.setWalletBalance(100.00);
        wallet.setDateCreated(LocalDateTime.now());

        walletRepository.save(wallet);

        // When
        Optional<Wallet> found = walletRepository.findByWalletAddress("wallet-abc-123");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getWalletBalance()).isEqualTo(100.00);
    }

    @Test
    @DisplayName("Should find all Wallets by wallet address")
    void shouldFindAllByWalletAddress() {
        // Given
        Wallet wallet1 = new Wallet();
        wallet1.setWalletAddress("wallet-xyz");
        wallet1.setWalletBalance(200.0);
        wallet1.setDateCreated(LocalDateTime.now());

        Wallet wallet2 = new Wallet();
        wallet2.setWalletAddress("wallet-xyz");
        wallet2.setWalletBalance(300.0);
        wallet2.setDateCreated(LocalDateTime.now());

        walletRepository.save(wallet1);
        walletRepository.save(wallet2);

        // When
        Optional<List<Wallet>> wallets = walletRepository.findAllByWalletAddress("wallet-xyz");

        // Then
        assertThat(wallets).isPresent();
        assertThat(wallets.get()).hasSize(2);
    }

    @Test
    @DisplayName("Should find Wallets created between two dates")
    void shouldFindWalletsByDateCreatedBetween() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earlier = now.minusDays(1);
        LocalDateTime later = now.plusDays(1);

        Wallet wallet = new Wallet();
        wallet.setWalletAddress("wallet-date");
        wallet.setWalletBalance(400.0);
        wallet.setDateCreated(now);

        walletRepository.save(wallet);

        // When
        Optional<List<Wallet>> found = walletRepository.findByDateCreatedBetween(earlier, later);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get()).isNotEmpty();
        assertThat(found.get().get(0).getWalletAddress()).isEqualTo("wallet-date");
    }
}
