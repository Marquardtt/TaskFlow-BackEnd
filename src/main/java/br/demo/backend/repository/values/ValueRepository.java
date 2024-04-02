package br.demo.backend.repository.values;

import br.demo.backend.model.values.TextValued;
import br.demo.backend.model.values.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueRepository extends JpaRepository<Value, Long> {


}
