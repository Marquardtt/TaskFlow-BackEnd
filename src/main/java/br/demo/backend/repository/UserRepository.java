package br.demo.backend.repository;

import br.demo.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

     User findByUsernameAndPassword(String email, String password);
     User findByMailAndPassword(String mail, String password);
     Collection<User> findAllByUsernameContainingOrNameContaining(String name, String userName);

}
