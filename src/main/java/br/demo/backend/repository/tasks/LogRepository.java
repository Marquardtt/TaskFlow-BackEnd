package br.demo.backend.repository.tasks;

import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Collection<Log> findAllByValue(PropertyValue value);
}
