package fr.fdr.jo_app.security.models;

import jakarta.persistence.*
        ;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotBlank
    @Size(max=50)
    @Email
    private String username;

    @NotBlank
    @Size(max=120)
    @Pattern(regexp = "(,=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,}", message = "Le mot de passe doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial. ")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String tokenUser;
    private String lastname;
    private String firstname;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public User() {

    }

    public void updateTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }



}