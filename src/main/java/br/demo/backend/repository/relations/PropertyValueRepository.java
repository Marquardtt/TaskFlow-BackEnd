package br.demo.backend.repository.relations;

import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.relations.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PropertyValueRepository extends JpaRepository<PropertyValue, Long> {
    public PropertyValue findTaskValuesByProperty_TypeAndValueContaining(TypeOfProperty type, Object value);
    public PropertyValue findByProperty_TypeAndValue(TypeOfProperty type, Object value);

    Collection<PropertyValue>  findAllByProperty_Id(Long id);
}
