package br.demo.backend.repository.properties;

import br.demo.backend.model.properties.relations.UserValue;
import br.demo.backend.model.properties.relations.ids.ValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserValueRepository extends JpaRepository<UserValue, ValueId> {
}
