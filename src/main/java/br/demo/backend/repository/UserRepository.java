package br.demo.backend.repository;

import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findByUsernameAndPassword(String email, String password);
    public User findByMailAndPassword(String mail, String password);
    public Collection<User> findAllByUsernameContainingOrNameContaining(String name, String userName);

    public Optional<User>findByUserDetailsEntity_Username(String username);
}
