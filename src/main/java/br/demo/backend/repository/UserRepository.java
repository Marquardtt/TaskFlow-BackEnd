package br.demo.backend.repository;

import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User>findByUserDetailsEntity_Username(String username);
    public Optional<User>findByUserDetailsEntity_UsernameContainingOrNameContaining(String username, String name);
}
