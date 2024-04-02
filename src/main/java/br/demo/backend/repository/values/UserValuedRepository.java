package br.demo.backend.repository.values;

import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.values.TextValued;
import br.demo.backend.model.values.UserValued;
import br.demo.backend.model.values.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserValuedRepository extends JpaRepository<UserValued, Long> {
    List<Value> findAllByUsersContaining(User user);

}
