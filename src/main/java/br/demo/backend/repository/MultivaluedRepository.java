package br.demo.backend.repository;

import br.demo.backend.model.Task;
import br.demo.backend.model.properties.Multivalued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultivaluedRepository extends JpaRepository<Multivalued, Long> {
}
