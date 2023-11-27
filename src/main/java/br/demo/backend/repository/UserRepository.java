package br.demo.backend.repository;

import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsernameAndPassword(String email, String password);
    public User findByMailAndPassword(String mail, String password);
    public User findByNameContains(String name);
}
