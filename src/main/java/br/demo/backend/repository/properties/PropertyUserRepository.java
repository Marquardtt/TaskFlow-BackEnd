package br.demo.backend.repository.properties;

import br.demo.backend.model.properties.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyUserRepository extends JpaRepository<PropertyUser, Long> {
}
