package fr.fdr.jo_app.security.repository;

import fr.fdr.jo_app.security.models.ERole;
import fr.fdr.jo_app.security.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
