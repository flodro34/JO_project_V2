package fr.fdr.jo_app;

import fr.fdr.jo_app.security.models.ERole;
import fr.fdr.jo_app.security.models.Role;
import fr.fdr.jo_app.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class JoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoAppApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Component
    public class RoleInitializer implements CommandLineRunner {

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public void run(String... args) throws Exception {
            if (roleRepository.findByName(ERole.UTILISATEUR).isEmpty()) {
                roleRepository.save(new Role(ERole.UTILISATEUR));
            }
            if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.ADMIN));
            }
        }
    }

}
