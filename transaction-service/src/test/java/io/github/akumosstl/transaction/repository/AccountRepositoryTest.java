package io.github.akumosstl.transaction.repository;

import io.github.akumosstl.transaction.model.Account;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Should save and retrieve Account by ID")
    void shouldSaveAndFindAccountById() {
        // Arrange
        Account account = new Account();
        account.setAccountNo("ACC123456");
        account.setFirstName("Alisson");
        account.setLastName("Pedrina");
        account.setEmail("alisson@example.com");
        account.setPhone("999999999");
        account.setSurname("Silva");

        // Act
        Account savedAccount = accountRepository.save(account);
        Optional<Account> found = accountRepository.findById(savedAccount.getAccountId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alisson@example.com");
        assertThat(found.get().getAccountNo()).isEqualTo("ACC123456");
    }
}

