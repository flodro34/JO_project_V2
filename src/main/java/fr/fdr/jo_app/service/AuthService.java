package fr.fdr.jo_app.service;

import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.UserRepository;
import fr.fdr.jo_app.security.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(SignupRequest user){
        Optional<User> oldUser = userRepository.findByUsername(user.getUsername());
        if(oldUser.isPresent()){
            User monUser = oldUser.get();
            monUser.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(monUser);
        }
    }

    public String generateTokenUser() {
        return UUID.randomUUID().toString(); // Génération d'un token unique
    }

    public void updateTokenUser(String username, String tokenUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        user.setTokenUser(tokenUser);
        userRepository.save(user);
    }
}