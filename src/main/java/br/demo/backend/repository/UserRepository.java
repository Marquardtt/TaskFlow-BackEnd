package br.demo.backend.repository;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsernameAndPassword(String email, String password);
    public User findByMailAndPassword(String mail, String password);
    public User findUserByUsernameOrName(String name, String userName);
}
