package io.github.akumosstl.transaction.repository;

import io.github.akumosstl.transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
