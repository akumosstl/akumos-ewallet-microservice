package io.github.akumosstl.account.repository;

import io.github.akumosstl.account.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Should find account by email or phone")
    void shouldFindByEmailOrPhone() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountNo("00000001");
        account.setEmail("test@example.com");
        account.setPhone("1234567890");
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setSurname("Smith");

        accountRepository.save(account);

        Optional<Account> byEmail = accountRepository.findByEmailOrPhone("test@example.com", "not-matching-phone");
        Optional<Account> byPhone = accountRepository.findByEmailOrPhone("not-matching-email", "1234567890");
        Optional<Account> byNone = accountRepository.findByEmailOrPhone("no@example.com", "000");

        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getFirstName()).isEqualTo("John");

        assertThat(byPhone).isPresent();
        assertThat(byPhone.get().getEmail()).isEqualTo("test@example.com");

        assertThat(byNone).isNotPresent();
    }
}

