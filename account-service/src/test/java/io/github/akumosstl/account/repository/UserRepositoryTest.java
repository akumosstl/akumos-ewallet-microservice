package io.github.akumosstl.account.repository;

import io.github.akumosstl.account.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("should find user by username")
    void testFindByUsername() {
        User user = new User();
        user.setUsername("john123");
        user.setEmail("john@example.com");
        user.setPassword("secret");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("john123");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("should return true if username exists")
    void testExistsByUsername() {
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("pwd");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("alice");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return true if email exists")
    void testExistsByEmail() {
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        user.setPassword("pwd");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("bob@example.com");
        assertThat(exists).isTrue();
    }
}

