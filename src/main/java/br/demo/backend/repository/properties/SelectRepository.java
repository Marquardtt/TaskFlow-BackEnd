package br.demo.backend.repository.properties;

import br.demo.backend.model.properties.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectRepository extends JpaRepository<Select, Long> {
}
