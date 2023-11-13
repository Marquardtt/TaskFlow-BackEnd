package br.demo.backend.repository;

import br.demo.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
