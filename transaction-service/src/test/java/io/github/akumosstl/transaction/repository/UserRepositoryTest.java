package io.github.akumosstl.transaction.repository;

import io.github.akumosstl.transaction.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and find user by username")
    void shouldFindUserByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("akumos");
        user.setEmail("akumos@example.com");
        user.setPassword("secret");

        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByUsername("akumos");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("akumos@example.com");
    }

    @Test
    @DisplayName("Should check if username exists")
    void shouldCheckExistsByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("checkme");
        user.setEmail("checkme@example.com");
        user.setPassword("pass");

        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByUsername("checkme");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckExistsByEmail() {
        // Arrange
        User user = new User();
        user.setUsername("checkemail");
        user.setEmail("exists@example.com");
        user.setPassword("pass");

        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("exists@example.com");

        // Assert
        assertThat(exists).isTrue();
    }
}

