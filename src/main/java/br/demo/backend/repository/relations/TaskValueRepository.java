package br.demo.backend.repository.relations;

import br.demo.backend.model.Configuration;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.model.values.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskValueRepository extends JpaRepository<TaskValue, Long> {
    public TaskValue findByProperty_TypeAndValue(TypeOfProperty type, Value value);
}
