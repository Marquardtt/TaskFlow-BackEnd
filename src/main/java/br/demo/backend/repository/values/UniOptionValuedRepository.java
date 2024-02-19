package br.demo.backend.repository.values;

import br.demo.backend.model.values.TextValued;
import br.demo.backend.model.values.UniOptionValued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniOptionValuedRepository extends JpaRepository<UniOptionValued, Long> {
}
