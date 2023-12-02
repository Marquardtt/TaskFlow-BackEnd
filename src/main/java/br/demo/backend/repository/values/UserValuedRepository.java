package br.demo.backend.repository.values;

import br.demo.backend.model.User;
import br.demo.backend.model.values.TextValued;
import br.demo.backend.model.values.UserValued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserValuedRepository extends JpaRepository<UserValued, Long> {

}
