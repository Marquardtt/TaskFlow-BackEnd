package br.demo.backend.repository.relations;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.relations.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskValueRepository extends JpaRepository<PropertyValue, Long> {
    public PropertyValue findTaskValuesByProperty_TypeAndValueContaining(TypeOfProperty type, Object value);
}
