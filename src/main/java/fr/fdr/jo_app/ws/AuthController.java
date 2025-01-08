package fr.fdr.jo_app.ws;

import fr.fdr.jo_app.security.jwt.JwtUtils;
import fr.fdr.jo_app.security.models.ERole;
import fr.fdr.jo_app.security.models.Role;
import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.RoleRepository;
import fr.fdr.jo_app.security.repository.UserRepository;
import fr.fdr.jo_app.security.request.LoginRequest;
import fr.fdr.jo_app.security.request.SignupRequest;
import fr.fdr.jo_app.security.response.JwtResponse;
import fr.fdr.jo_app.security.response.MessageResponse;
import fr.fdr.jo_app.security.service.UserDetailsImpl;
import fr.fdr.jo_app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génération du JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Génération et mise à jour du tokenUser
        String tokenUser = authService.generateTokenUser();
        authService.updateTokenUser(loginRequest.getUsername(), tokenUser);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                tokenUser));
    }

    @PutMapping("/changePassword")
    public void changePassword(@Valid @RequestBody SignupRequest signupRequest) {
        this.authService.changePassword(signupRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        // Generate tokenUser
        String tokenUser = authService.generateTokenUser();
        user.setTokenUser(tokenUser);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.UTILISATEUR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.UTILISATEUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        // Response with tokenUser
        return ResponseEntity.ok(new JwtResponse(
                null,
                user.getIdUser(),
                user.getUsername(),
                roles.stream().map(Role::getName).map(Enum::name).collect(Collectors.toList()),
                tokenUser
        ));

    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Endpoint accessible!";
    }

}
