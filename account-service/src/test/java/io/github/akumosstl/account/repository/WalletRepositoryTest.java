package io.github.akumosstl.account.repository;

import io.github.akumosstl.account.model.Account;
import io.github.akumosstl.account.model.Wallet;
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

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Should save and retrieve wallet by ID")
    void shouldSaveAndFindWallet() {
        Account account = new Account();
        account.setAccountId(23L);  // important!
        account.setAccountNo("00000009");
        account.setEmail("walletuser@example.com");
        account.setPhone("9998887777");
        account.setFirstName("Wallet");
        account.setLastName("User");
        account.setSurname("Example");

        Account savedAccount = accountRepository.save(account);

        Wallet wallet = new Wallet();
        wallet.setAccount(savedAccount);
        wallet.setWalletAddress("abc-xyz-123");
        wallet.setWalletBalance(1000.50);
        wallet.setDateCreated(LocalDateTime.now());

        Wallet savedWallet = walletRepository.save(wallet);

        Optional<Wallet> found = walletRepository.findById(savedWallet.getWalletId());

        assertThat(found).isPresent();
        assertThat(found.get().getWalletAddress()).isEqualTo("abc-xyz-123");
        assertThat(found.get().getAccount().getEmail()).isEqualTo("walletuser@example.com");
    }
}

