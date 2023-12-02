package br.demo.backend.repository.values;

import br.demo.backend.model.values.NumberValued;
import br.demo.backend.model.values.TextValued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberValuedRepository extends JpaRepository<NumberValued, Long> {
}
