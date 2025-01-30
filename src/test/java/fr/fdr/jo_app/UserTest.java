package fr.fdr.jo_app;

import fr.fdr.jo_app.security.config.WebSecurityConfig;
import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.UserRepository;
import fr.fdr.jo_app.services.UserServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(WebSecurityConfig.class)
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceTest userServiceTest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setup() {
        // Clean bdd before
        userRepository.deleteAll();

        // Create and save User
        user = new User("testuser@example.com", passwordEncoder.encode("password123"));
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setTokenUser("testUserToken");
        user = userRepository.save(user);
    }

    @Test
    void testCreateAndRetrieveUser() {
        // Act
        User retrievedUser = userServiceTest.findUserByUsername("testuser@example.com").orElse(null);

        // Assert
        assertNotNull(retrievedUser, "L'utilisateur devrait exister !");
        assertEquals("testuser@example.com", retrievedUser.getUsername());
        assertTrue(passwordEncoder.matches("password123", retrievedUser.getPassword()));
    }

    @Test
    void testPasswordEncoding() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = user.getIdUser();

        // Act
        userRepository.delete(user);
        Optional<User> deletedUser = userRepository.findById(userId);

        // Assert
        assertTrue(deletedUser.isEmpty(), "L'utilisateur devrait être supprimé !");
    }

    @Test
    void testUpdateUser() {
        // Arrange
        user.setFirstname("Jane");

        // Act
        user = userRepository.save(user);
        User updatedUser = userRepository.findById(user.getIdUser()).orElse(null);

        // Assert
        assertNotNull(updatedUser, "L'utilisateur mis à jour ne devrait pas être null !");
        assertEquals("Jane", updatedUser.getFirstname(), "Le prénom de l'utilisateur n'a pas été mis à jour !");
    }
}

